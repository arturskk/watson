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

    public Event addProduct(final AddProductData data) {
        return this.eventStore.storeEvent(Product.PRODUCT_STREAM, asEventPayload(data));
    }

    private ProductAdded asEventPayload(final AddProductData data) {
        return ProductAdded
                .builder()
                .name(data.getName())
                .categoryUuid(data.getCategoryUuid())
                .build();
    }

}
