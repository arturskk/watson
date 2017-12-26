package net.lipecki.watson.product;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.event.EventStore;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AddProductCommand {

    private final EventStore eventStore;

    public AddProductCommand(final EventStore eventStore) {
        this.eventStore = eventStore;
    }

    public Event<AddProduct> addProduct(final AddProduct addProduct) {
        return this.eventStore.storeEvent(Product.PRODUCT_STREAM, addProduct);
    }

}
