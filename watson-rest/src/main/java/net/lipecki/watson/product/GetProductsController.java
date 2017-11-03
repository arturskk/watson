package net.lipecki.watson.product;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.rest.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(Api.V1)
public class GetProductsController {

    private final GetProductsQuery query;

    public GetProductsController(final GetProductsQuery query) {
        this.query = query;
    }

    @GetMapping("/product")
    public List<Product> getAllProducts() {
        return this.query.getProducts();
    }

}
