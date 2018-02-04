package net.lipecki.watson.category;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.rest.Api;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(Api.V1)
public class GetCategoriesController {

    private GetCategoriesQuery getCategoriesQuery;

    private GetCategoryQuery getCategoryQuery;

    public GetCategoriesController(final GetCategoriesQuery getCategoriesQuery, final GetCategoryQuery getCategoryQuery) {
        this.getCategoriesQuery = getCategoriesQuery;
        this.getCategoryQuery = getCategoryQuery;
    }

    @GetMapping("/category/{categoryType}")
    @Transactional
    public List<ListCategoryDto> getAllCategories(@PathVariable final String categoryType) {
        return this.getCategoriesQuery
                .getCategories(categoryType)
                .stream()
                .map(ListCategoryDto::from)
                .collect(Collectors.toList());
    }

    @GetMapping("/category/tree/{categoryType}")
    @Transactional
    public CategoryTreeDto getCategoriesTree(@PathVariable final String categoryType) {
        final Category rootCategory = this.getCategoryQuery.getRootCategory();
        return CategoryTreeDto.from(rootCategory, categoryType);
    }

}
