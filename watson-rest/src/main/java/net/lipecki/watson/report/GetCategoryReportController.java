package net.lipecki.watson.report;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.WatsonException;
import net.lipecki.watson.category.Category;
import net.lipecki.watson.expanse.Expanse;
import net.lipecki.watson.expanse.ExpanseCost;
import net.lipecki.watson.expanse.GetExpansesQuery;
import net.lipecki.watson.rest.Api;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(Api.V1)
public class GetCategoryReportController {

    private static final DateTimeFormatter fullDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final GetExpansesQuery expansesQuery;

    public GetCategoryReportController(final GetExpansesQuery expansesQuery) {
        this.expansesQuery = expansesQuery;
    }

    @GetMapping("/report/category")
    public CategoryReportDto getCategoryReportDto(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate from,
                                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate to) {
        log.debug("Request for categories report [from=[}, to=[}]", from, to);
        final List<Expanse> expanses = expansesQuery.getExpanses(from, to);

        // Get all categories based on expanses within range
        final Map<Category, List<Expanse>> categoryToExpanse = expanses
                .stream()
                .collect(Collectors.groupingBy(Expanse::getCategory));

        // Build categories index (for quick access during tree building)
        final Map<String, CategoryReportDto.CategorySummary> categories = categoryToExpanse
                .keySet()
                .stream()
                .map(this::asDto)
                .collect(Collectors.toMap(CategoryReportDto.CategorySummary::getUuid, Function.identity()));
        // Build categories tree
        categoryToExpanse
                .keySet()
                .stream()
                .filter(category -> category.getParent().isPresent())
                .forEach(
                        category -> {
                            final CategoryReportDto.CategorySummary parent = category.getParent().map(Category::getUuid).map(categories::get).orElseThrow(() -> WatsonException.of("Categories should be filtered before!"));
                            parent.addSubCategory(categories.get(category.getUuid()));
                        }
                );
        // Calculate each category summary costs
        categoryToExpanse
                .entrySet()
                .stream()
                .map(
                        entry -> new AbstractMap.SimpleEntry<>(
                                entry.getKey(),
                                entry.getValue().stream().map(Expanse::getCost).reduce(ExpanseCost.ZERO, ExpanseCost::add)
                        )
                )
                .forEach(entry -> categories.get(entry.getKey().getUuid()).setCategoryCost(entry.getValue()));
        // Get tree root
        final CategoryReportDto.CategorySummary rootCategory = categories.get(Category.ROOT_UUID);

        return CategoryReportDto
                .builder()
                .from(from != null ? fullDateFormat.format(from) : null)
                .to(to != null ? fullDateFormat.format(to) : null)
                .rootCategory(rootCategory)
                .build();
    }

    private CategoryReportDto.CategorySummary asDto(final Category category) {
        return CategoryReportDto
                .CategorySummary
                .builder()
                .uuid(category.getUuid())
                .name(category.getName())
                .build();
    }

}
