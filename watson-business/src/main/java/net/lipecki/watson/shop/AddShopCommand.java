package net.lipecki.watson.shop;

import net.lipecki.watson.store.Event;
import net.lipecki.watson.store.EventStore;
import org.springframework.stereotype.Service;

@Service
public class AddShopCommand {

    public static final String ADD_SHOP_EVENT = "_shop_add";
    private EventStore eventStore;

    public AddShopCommand(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    public Event<AddShop> addShop(final AddShop addShop) {
        return this.eventStore.storeEvent(Shop.SHOP_STREAM, ADD_SHOP_EVENT, addShop);
    }

}
