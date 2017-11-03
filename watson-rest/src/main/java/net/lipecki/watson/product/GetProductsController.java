package net.lipecki.watson.product;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
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
