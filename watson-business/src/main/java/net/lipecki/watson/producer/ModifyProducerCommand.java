package net.lipecki.watson.producer;

import net.lipecki.watson.event.Event;
import net.lipecki.watson.event.EventStore;
import org.springframework.stereotype.Service;

@Service
public class ModifyProducerCommand {

    private final EventStore eventStore;

    public ModifyProducerCommand(final EventStore eventStore) {
        this.eventStore = eventStore;
    }

    public Event modifyProducer(final ModifyProducerData data) {
        return this.eventStore.storeEvent(Producer.PRODUCER_STREAM, asEventPayload(data));
    }

    private ProducerModified asEventPayload(final ModifyProducerData data) {
        return ProducerModified
                .builder()
                .uuid(data.getUuid())
                .name(data.getName())
                .build();
    }

}
