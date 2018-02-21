package net.lipecki.watson.shop;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.rest.Api;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
        log.info("Request to modify shop [uuid={}, dto={}]", uuid, dto);
        return modifyShopCommand.modifyShop(
                ModifyShop
                        .builder()
                        .uuid(uuid)
                        .name(dto.getName())
                        .retailChainUuid(dto.getRetailChainUuid())
                        .build()
        );
    }

}
