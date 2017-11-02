package net.lipecki.watson.category;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class GetCategoriesController {

    @GetMapping("/category")
    public String getAllCategories() {
        return "ok";
    }

}
