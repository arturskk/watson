package net.lipecki.watson.shop;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.rest.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(Api.V1)
public class AddShopController {

    private final AddShopCommand addShopCommand;

    public AddShopController(final AddShopCommand addShopCommand) {
        this.addShopCommand = addShopCommand;
    }

    @PostMapping("/shop")
    public Event<AddShop> addShop(@Validated @RequestBody AddShopDto dto) {
        log.info("Request to add shop [dto={}]", dto);
        return addShopCommand.addShop(
                AddShop.builder()
                        .name(dto.getName())
                        .build()
        );
    }

}