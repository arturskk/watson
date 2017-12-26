package net.lipecki.watson.shop;

import net.lipecki.watson.combiner.AggregateCombinerHandler;
import net.lipecki.watson.event.Event;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ShopAddedEventHandler implements AggregateCombinerHandler<Shop, ShopAdded> {

    @Override
    public Class<ShopAdded> getPayloadClass() {
        return ShopAdded.class;
    }

    @Override
    public void accept(final Map<String, Shop> collection, final Event event, final ShopAdded payload) {
        collection.put(
                event.getStreamId(),
                Shop.builder()
                        .uuid(event.getStreamId())
                        .name(payload.getName())
                        .build()
        );
    }

}
