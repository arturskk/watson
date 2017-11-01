package net.lipecki.watson.category;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AddCategoryController {

    private final AddCategoryService service;

    public AddCategoryController(AddCategoryService service) {
        this.service = service;
    }

    @PostMapping("/category")
    public String addCategory(@Validated @RequestBody AddCategoryDto dto) {
        log.info("Request to add category [dto={}]", dto);
        return service.addCategory(dto.getType(), dto.getName());
    }

}
