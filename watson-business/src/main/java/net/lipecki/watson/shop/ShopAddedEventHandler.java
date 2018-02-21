package net.lipecki.watson.shop;

import net.lipecki.watson.combiner.AggregateCombinerHandler;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.reatialchain.GetRetailChainQuery;
import net.lipecki.watson.reatialchain.RetailChain;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ShopAddedEventHandler implements AggregateCombinerHandler<Shop, ShopAdded> {

    private final GetRetailChainQuery retailChainQuery;

    public ShopAddedEventHandler(final GetRetailChainQuery retailChainQuery) {
        this.retailChainQuery = retailChainQuery;
    }

    @Override
    public Class<ShopAdded> getPayloadClass() {
        return ShopAdded.class;
    }

    @Override
    public void accept(final Map<String, Shop> collection, final Event event, final ShopAdded payload) {
        final RetailChain retailChain = payload
                .getRetailChainUuidOptional()
                .flatMap(retailChainQuery::getRetailChain)
                .orElse(null);
        collection.put(
                event.getStreamId(),
                Shop.builder()
                        .uuid(event.getStreamId())
                        .name(payload.getName())
                        .retailChain(retailChain)
                        .build()
        );
    }

}
