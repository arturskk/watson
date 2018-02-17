package net.lipecki.watson.reatialchain;

import net.lipecki.watson.event.Event;
import net.lipecki.watson.event.EventStore;
import org.springframework.stereotype.Service;

@Service
public class AddRetailChainCommand {

    private final EventStore eventStore;

    public AddRetailChainCommand(final EventStore eventStore) {
        this.eventStore = eventStore;
    }

    public Event addRetailChain(final AddRetailChainData data) {
        return this.eventStore.storeEvent(RetailChain.RETAIL_CHAIN_STREAM, asEventPayload(data));
    }

    private RetailChainAdded asEventPayload(final AddRetailChainData data) {
        return RetailChainAdded
                .builder()
                .name(data.getName())
                .build();
    }

}
