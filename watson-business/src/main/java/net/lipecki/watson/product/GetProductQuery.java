package net.lipecki.watson.product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class GetProductQuery {

    private final ProductStore productStore;

    public GetProductQuery(final ProductStore productStore) {
        this.productStore = productStore;
    }

    public Optional<Product> getProduct(final String uuid) {
        return this.productStore.getProduct(uuid);
    }

}
