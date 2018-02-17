package net.lipecki.watson.reatialchain;

import net.lipecki.watson.combiner.AggregateCombinerHandler;
import net.lipecki.watson.event.Event;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RetailChainModifiedEventHandler implements AggregateCombinerHandler<RetailChain, RetailChainModified> {

    @Override
    public Class<RetailChainModified> getPayloadClass() {
        return RetailChainModified.class;
    }

    @Override
    public void accept(final Map<String, RetailChain> collection, final Event event, final RetailChainModified payload) {
        final RetailChain model = collection.get(payload.getUuid());
        payload
                .getNameOptional()
                .ifPresent(model::setName);
    }

}
