package net.lipecki.watson.shop;

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
public class ModifyShopController {

    private final ModifyShopCommand modifyShopCommand;

    public ModifyShopController(final ModifyShopCommand modifyShopCommand) {
        this.modifyShopCommand = modifyShopCommand;
    }

    @PutMapping("/shop/{uuid}")
    @Transactional
    public Event modifyProduct(
            @PathVariable final String uuid,
            @Validated @RequestBody final ModifyShopDto dto) {
        log.info("Request to modify product [uuid={}, dto={}]", uuid, dto);
        return modifyShopCommand.modifyShop(
                ModifyShop
                        .builder()
                        .uuid(uuid)
                        .name(dto.getName())
                        .build()
        );
    }

}
