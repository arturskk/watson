package net.lipecki.watson.category;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.rest.Api;
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

    private GetCategoriesQuery query;

    public GetCategoriesController(GetCategoriesQuery query) {
        this.query = query;
    }

    @GetMapping("/category/{categoryType}")
    public List<ListCategoryDto> getAllCategories(@PathVariable final String categoryType) {
        return this.query
                .getCategories(categoryType)
                .stream()
                .map(GetCategoriesController::asListCategoryDto)
                .collect(Collectors.toList());
    }

    private static ListCategoryDto asListCategoryDto(final Category category) {
        return ListCategoryDto
                .builder()
                .parentUuid(category.getParent().map(Category::getUuid).orElse(null))
                .type(category.getType())
                .uuid(category.getUuid())
                .name(category.getName())
                .path(category.getCategoryPath())
                .build();
    }

}
