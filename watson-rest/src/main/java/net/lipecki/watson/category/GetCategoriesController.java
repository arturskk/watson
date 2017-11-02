package net.lipecki.watson.category;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class GetCategoriesController {

    private GetCategoriesQuery query;

    public GetCategoriesController(GetCategoriesQuery query) {
        this.query = query;
    }

    @GetMapping("/category/{categoryType}")
    public List<Category> getAllCategories(@PathVariable final String categoryType) {
        return this.query.getCategories(categoryType);
    }

}
