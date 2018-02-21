package net.lipecki.watson.shop;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.event.EventStore;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ModifyShopCommand {

    private final EventStore eventStore;

    public ModifyShopCommand(final EventStore eventStore) {
        this.eventStore = eventStore;
    }

    public Event modifyShop(final ModifyShop data) {
        return this.eventStore.storeEvent(Shop.SHOP_STREAM, asEventPayload(data));
    }

    private ShopModified asEventPayload(final ModifyShop data) {
        return ShopModified
                .builder()
                .uuid(data.getUuid())
                .name(data.getName())
                .retailChainUuid(data.getRetailChainUuid())
                .build();
    }

}
