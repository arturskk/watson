package net.lipecki.watson.product;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.combiner.AggregateStreamCombiner;
import net.lipecki.watson.event.EventStore;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class ProductStore {

    private final AggregateStreamCombiner<Product> combiner;

    public ProductStore(
            final EventStore eventStore,
            final AddProductEventHandler addProductEventHandler,
            final ModifyProductEventHandler modifyProductEventHandler) {
        this.combiner = new AggregateStreamCombiner<>(eventStore, Product.PRODUCT_STREAM);
        this.combiner.registerHandler(AddProductCommand.ADD_PRODUCT_EVENT, addProductEventHandler);
        this.combiner.registerHandler(ModifyProductCommand.MODIFY_PRODUCT_EVENT, modifyProductEventHandler);
    }

    public Map<String, Product> getProducts() {
        return this.combiner.get();
    }

}
