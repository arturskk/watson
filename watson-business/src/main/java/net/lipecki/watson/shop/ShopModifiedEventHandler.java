package net.lipecki.watson.shop;

import net.lipecki.watson.combiner.AggregateCombinerHandler;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.reatialchain.GetRetailChainQuery;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ShopModifiedEventHandler implements AggregateCombinerHandler<Shop, ShopModified> {

    private GetRetailChainQuery retailChainQuery;

    public ShopModifiedEventHandler(GetRetailChainQuery retailChainQuery) {
        this.retailChainQuery = retailChainQuery;
    }

    @Override
    public Class<ShopModified> getPayloadClass() {
        return ShopModified.class;
    }

    @Override
    public void accept(final Map<String, Shop> collection, final Event event, final ShopModified payload) {
        final Shop shop = collection.get(payload.getUuid());
        payload
                .getRetailChainUuidOptional()
                .flatMap(retailChainQuery::getRetailChain)
                .ifPresent(shop::setRetailChain);
        payload
                .getNameOptional()
                .ifPresent(shop::setName);
    }

}
