package net.lipecki.watson.product;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.combiner.AggregateStreamCombiner;
import net.lipecki.watson.store.EventStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GetProductsQuery {

    private final AggregateStreamCombiner<Product> combiner;

    public GetProductsQuery(final EventStore eventStore) {
        this.combiner = new AggregateStreamCombiner<>(eventStore, Product.PRODUCT_STREAM);
        this.combiner.registerHandler(
                AddProductCommand.ADD_PRODUCT_EVENT,
                (collection, event) -> collection.put(
                        event.getAggregateId(),
                        Product.builder()
                                .uuid(event.getAggregateId())
                                .name(event.castPayload(AddProduct.class).getName())
                                .build()
                )
        );
    }

    public List<Product> getProducts() {
        return this.combiner.getAsList();
    }

}
