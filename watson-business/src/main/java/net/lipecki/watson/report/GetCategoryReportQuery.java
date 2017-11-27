package net.lipecki.watson.report;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.category.Category;
import net.lipecki.watson.category.GetCategoryQuery;
import net.lipecki.watson.expanse.Expanse;
import net.lipecki.watson.expanse.ExpanseCost;
import net.lipecki.watson.expanse.GetExpansesQuery;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GetCategoryReportQuery {

    private static final DateTimeFormatter fullDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final GetExpansesQuery expansesQuery;
    private final GetCategoryQuery categoryQuery;

    public GetCategoryReportQuery(final GetExpansesQuery expansesQuery, final GetCategoryQuery categoryQuery) {
        this.expansesQuery = expansesQuery;
        this.categoryQuery = categoryQuery;
    }

    public CategoryReport getCategoryReport(final LocalDate from, final LocalDate to) {
        final Map<String, ExpanseCost> categoryExpanses = getCategoryToExpanseMapping(expansesQuery.getExpanses(from, to));
        final CategoryReportItem reportRoot = getReportTree(categoryExpanses);
        return CategoryReport
                .builder()
                .from(getFormattedDate(from))
                .to(getFormattedDate(to))
                .rootCategory(reportRoot)
                .build();
    }

    private CategoryReportItem getReportTree(final Map<String, ExpanseCost> categoryExpanses) {
        final Map<String, CategoryReportItem> entriesIndex = new HashMap<>();
        final Category rootCategory = this.categoryQuery.getRootCategory();
        rootCategory.accept(
                category -> {
                    final CategoryReportItem entry = asCategoryEntry(category, categoryExpanses.get(category.getUuid()));
                    entriesIndex.put(category.getUuid(), entry);
                    category.getParent()
                            .map(Category::getUuid)
                            .map(entriesIndex::get)
                            .ifPresent(entry::addParent);
                }
        );
        return entriesIndex.get(rootCategory.getUuid());
    }

    private Map<String, ExpanseCost> getCategoryToExpanseMapping(final List<Expanse> expanses) {
        return expanses
                .stream()
                .collect(Collectors.groupingBy(Expanse::getCategory))
                .entrySet()
                .stream()
                .map(e -> new AbstractMap.SimpleEntry<>(e.getKey().getUuid(), getSummaryCost(e.getValue())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private String getFormattedDate(final LocalDate from) {
        return from != null ? fullDateFormat.format(from) : null;
    }

    private ExpanseCost getSummaryCost(final List<Expanse> expanses) {
        return expanses.stream().map(Expanse::getCost).reduce(ExpanseCost.ZERO, ExpanseCost::add);
    }

    private CategoryReportItem asCategoryEntry(final Category category, final ExpanseCost expanseCost) {
        return CategoryReportItem
                .builder()
                .uuid(category.getUuid())
                .name(category.getName())
                .type(category.getType())
                .categoryCost(expanseCost != null ? expanseCost : ExpanseCost.ZERO)
                .build();
    }

}
