package net.lipecki.watson.shop;

import net.lipecki.watson.combiner.AggregateCombinerHandler;
import net.lipecki.watson.event.Event;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ShopModifiedEventHandler implements AggregateCombinerHandler<Shop, ShopModified> {

    @Override
    public Class<ShopModified> getPayloadClass() {
        return ShopModified.class;
    }

    @Override
    public void accept(final Map<String, Shop> collection, final Event event, final ShopModified payload) {
        final Shop shop = collection.get(payload.getUuid());
        payload
                .getNameOptional()
                .ifPresent(shop::setName);
    }

}
