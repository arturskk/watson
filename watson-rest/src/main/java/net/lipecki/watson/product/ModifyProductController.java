package net.lipecki.watson.product;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.rest.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(Api.V1)
public class ModifyProductController {

    private final ModifyProductCommand modifyProductCommand;

    public ModifyProductController(final ModifyProductCommand modifyProductCommand) {
        this.modifyProductCommand = modifyProductCommand;
    }

    @PutMapping("/product/{uuid}")
    public Event<ModifyProduct> modifyProduct(
            @PathVariable final String uuid,
            @Validated @RequestBody final ModifyProductDto dto) {
        log.info("Request to modify product [uuid={}, dto={}]", uuid, dto);
        return modifyProductCommand.modifyProduct(
                ModifyProduct
                        .builder()
                        .uuid(uuid)
                        .name(dto.getName())
                        .categoryUuid(dto.getCategoryUuid())
                        .build()
        );
    }

}
