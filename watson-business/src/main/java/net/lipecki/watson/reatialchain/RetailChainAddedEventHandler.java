package net.lipecki.watson.reatialchain;

import net.lipecki.watson.combiner.AggregateCombinerHandler;
import net.lipecki.watson.event.Event;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RetailChainAddedEventHandler implements AggregateCombinerHandler<RetailChain, RetailChainAdded> {

    @Override
    public Class<RetailChainAdded> getPayloadClass() {
        return RetailChainAdded.class;
    }

    @Override
    public void accept(final Map<String, RetailChain> collection, final Event event, final RetailChainAdded payload) {
        collection.put(
                event.getStreamId(),
                RetailChain.builder()
                        .uuid(event.getStreamId())
                        .name(payload.getName())
                        .build()
        );
    }

}
