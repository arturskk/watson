package net.lipecki.watson.report.categoryexpanse;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.category.Category;
import net.lipecki.watson.category.GetCategoryQuery;
import net.lipecki.watson.cost.Cost;
import net.lipecki.watson.expanse.Expanse;
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
public class GetCategoryExpanseReportQuery {

    private static final DateTimeFormatter fullDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final GetExpansesQuery expansesQuery;
    private final GetCategoryQuery categoryQuery;

    public GetCategoryExpanseReportQuery(final GetExpansesQuery expansesQuery, final GetCategoryQuery categoryQuery) {
        this.expansesQuery = expansesQuery;
        this.categoryQuery = categoryQuery;
    }

    public CategoryExpanseReport getCategoryReport(final LocalDate from, final LocalDate to) {
        final Map<String, Cost> categoryExpanses = getCategoryToExpanseMapping(expansesQuery.getExpanses(from, to));
        final CategoryExpanseReportItem reportRoot = getReportTree(categoryExpanses);
        return CategoryExpanseReport
                .builder()
                .from(getFormattedDate(from))
                .to(getFormattedDate(to))
                .rootCategory(reportRoot)
                .build();
    }

    private CategoryExpanseReportItem getReportTree(final Map<String, Cost> categoryExpanses) {
        final Map<String, CategoryExpanseReportItem> entriesIndex = new HashMap<>();
        final Category rootCategory = this.categoryQuery.getRootCategory();
        rootCategory.accept(
                category -> {
                    final CategoryExpanseReportItem entry = asCategoryEntry(category, categoryExpanses.get(category.getUuid()));
                    entriesIndex.put(category.getUuid(), entry);
                    category.getParent()
                            .map(Category::getUuid)
                            .map(entriesIndex::get)
                            .ifPresent(entry::addParent);
                }
        );
        return entriesIndex.get(rootCategory.getUuid());
    }

    private Map<String, Cost> getCategoryToExpanseMapping(final List<Expanse> expanses) {
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

    private Cost getSummaryCost(final List<Expanse> expanses) {
        return expanses.stream().map(Expanse::getCost).reduce(Cost.ZERO, Cost::add);
    }

    private CategoryExpanseReportItem asCategoryEntry(final Category category, final Cost cost) {
        return CategoryExpanseReportItem
                .builder()
                .uuid(category.getUuid())
                .name(category.getName())
                .type(category.getType())
                .categoryCost(cost != null ? cost : Cost.ZERO)
                .build();
    }

}
