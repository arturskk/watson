package net.lipecki.watson.producer;

import net.lipecki.watson.event.Event;
import net.lipecki.watson.event.EventStore;
import org.springframework.stereotype.Service;

@Service
public class AddProducerCommand {

    private final EventStore eventStore;

    public AddProducerCommand(final EventStore eventStore) {
        this.eventStore = eventStore;
    }

    public Event addProducer(final AddProducerData data) {
        return this.eventStore.storeEvent(Producer.PRODUCER_STREAM, asEventPayload(data));
    }

    private ProducerAdded asEventPayload(final AddProducerData data) {
        return ProducerAdded
                .builder()
                .name(data.getName())
                .build();
    }

}
