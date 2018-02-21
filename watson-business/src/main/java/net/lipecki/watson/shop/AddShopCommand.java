package net.lipecki.watson.shop;

import net.lipecki.watson.event.Event;
import net.lipecki.watson.event.EventStore;
import org.springframework.stereotype.Service;

@Service
public class AddShopCommand {

    private EventStore eventStore;

    public AddShopCommand(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    public Event addShop(final AddShopData data) {
        return this.eventStore.storeEvent(Shop.SHOP_STREAM, asEventPayload(data));
    }

    private ShopAdded asEventPayload(final AddShopData data) {
        return ShopAdded
                .builder()
                .name(data.getName())
                .retailChainUuid(data.getRetailChainUuid())
                .build();
    }

}
