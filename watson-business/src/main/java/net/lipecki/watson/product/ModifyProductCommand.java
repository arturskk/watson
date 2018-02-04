package net.lipecki.watson.product;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.event.EventStore;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ModifyProductCommand {

    private final EventStore eventStore;

    public ModifyProductCommand(final EventStore eventStore) {
        this.eventStore = eventStore;
    }

    public Event modifyProduct(final ModifyProduct data) {
        return this.eventStore.storeEvent(Product.PRODUCT_STREAM, asEventPayload(data));
    }

    private ProductModified asEventPayload(final ModifyProduct data) {
        return ProductModified
                .builder()
                .uuid(data.getUuid())
                .name(data.getName())
                .defaultUnit(data.getDefaultUnit())
                .categoryUuid(data.getCategoryUuid())
                .build();
    }

}
