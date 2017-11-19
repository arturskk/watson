package net.lipecki.watson.product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class GetProductsQuery {

    private final ProductStore productStore;

    public GetProductsQuery(final ProductStore productStore) {
        this.productStore = productStore;
    }

    public List<Product> getProducts() {
        final Map<String, Product> products = this.productStore.getProducts();
        return new ArrayList<>(products.values());
    }

}
