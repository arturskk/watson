package net.lipecki.watson.product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GetProductsQuery {

    private final ProductStore productStore;

    public GetProductsQuery(final ProductStore productStore) {
        this.productStore = productStore;
    }

    public List<Product> getProducts() {
        return this.productStore
                .getProducts()
                .stream()
                .sorted(Comparator.comparing(Product::getName))
                .collect(Collectors.toList());
    }

}
