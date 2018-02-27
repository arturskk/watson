package net.lipecki.watson.product;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.combiner.AggregateCombiner;
import net.lipecki.watson.combiner.AggregateCombinerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
class ProductStore {

    private final AggregateCombiner<Product> combiner;

    public ProductStore(
            final AggregateCombinerFactory aggregateCombinerFactory,
            final ProductAddedEventHandler productAddedEventHandler,
            final ProductModifiedEventHandler productModifiedEventHandler) {
        this.combiner = aggregateCombinerFactory.getAggregateCombiner(getClass().getSimpleName(), Collections.singletonList(Product.PRODUCT_STREAM));
        this.combiner.addHandler(productAddedEventHandler);
        this.combiner.addHandler(productModifiedEventHandler);
    }

    public List<Product> getProducts() {
        return this.combiner.getAsList();
    }

    public Optional<Product> getProduct(final String uuid) {
        return Optional.ofNullable(
                this.combiner.get().get(uuid)
        );
    }

}
