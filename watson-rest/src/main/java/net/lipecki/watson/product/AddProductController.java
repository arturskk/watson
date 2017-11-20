package net.lipecki.watson.product;

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
public class AddProductController {

    private final AddProductCommand addProductCommand;

    public AddProductController(final AddProductCommand addProductCommand) {
        this.addProductCommand = addProductCommand;
    }

    @PostMapping("/product")
    public Event<AddProduct> addCategory(@Validated @RequestBody AddProductDto dto) {
        log.info("Request to add category [dto={}]", dto);
        return addProductCommand.addProduct(
                AddProduct.builder()
                        .name(dto.getName())
                        .categoryUuid(dto.getCategoryUuid())
                        .build()
        );
    }

}
