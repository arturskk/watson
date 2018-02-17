package net.lipecki.watson.reatialchain;

import net.lipecki.watson.event.Event;
import net.lipecki.watson.event.EventStore;
import org.springframework.stereotype.Service;

@Service
public class ModifyRetailChainCommand {

    private final EventStore eventStore;

    public ModifyRetailChainCommand(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    public Event modifyRetailChain(final ModifyRetailChainData data) {
        return this.eventStore.storeEvent(RetailChain.RETAIL_CHAIN_STREAM, asEventPayload(data));
    }

    private RetailChainModified asEventPayload(final ModifyRetailChainData data) {
        return RetailChainModified
                .builder()
                .uuid(data.getUuid())
                .name(data.getName())
                .build();
    }

}
