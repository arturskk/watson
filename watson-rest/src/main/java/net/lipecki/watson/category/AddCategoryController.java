package net.lipecki.watson.category;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.rest.Api;
import net.lipecki.watson.event.Event;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(Api.V1)
public class AddCategoryController {

    private final AddCategoryCommand addCategoryCommand;

    public AddCategoryController(AddCategoryCommand addCategoryCommand) {
        this.addCategoryCommand = addCategoryCommand;
    }

    @PostMapping("/category")
    public Event<AddCategory> addCategory(@Validated @RequestBody AddCategoryDto dto) {
        log.info("Request to add category [dto={}]", dto);
        return addCategoryCommand.addCategory(
                AddCategory.builder()
                        .type(dto.getType())
                        .name(dto.getName())
                        .build()
        );
    }

}
