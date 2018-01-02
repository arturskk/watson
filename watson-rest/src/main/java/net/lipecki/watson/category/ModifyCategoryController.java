package net.lipecki.watson.category;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.rest.Api;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(Api.V1)
public class ModifyCategoryController {

    private final ModifyCategoryCommand modifyCategoryCommand;

    public ModifyCategoryController(final ModifyCategoryCommand modifyCategoryCommand) {
        this.modifyCategoryCommand = modifyCategoryCommand;
    }

    @PutMapping("/category/{uuid}")
    @Transactional
    public Event modifyProduct(
            @PathVariable final String uuid,
            @Validated @RequestBody final ModifyCategoryDto dto) {
        log.info("Request to modify category [uuid={}, dto={}]", uuid, dto);
        return modifyCategoryCommand.modifyCategory(
                ModifyCategoryData
                        .builder()
                        .uuid(uuid)
                        .name(dto.getName())
                        .parentUuid(dto.getParentUuid())
                        .build()
        );
    }

}
