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

    public Event<AddShop> addShop(final AddShop addShop) {
        return this.eventStore.storeEvent(Shop.SHOP_STREAM, addShop);
    }

}
