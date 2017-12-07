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

    public Event<ModifyProduct> modifyProduct(final ModifyProduct modifyProduct) {
        return this.eventStore.storeEvent(
                Product.PRODUCT_STREAM,
                modifyProduct
        );
    }

}
