package net.lipecki.watson.event;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class InMemoryEventStore implements EventStore {

    private List<Event<?>> store = new ArrayList<>();
    private AtomicLong eventIdSequence = new AtomicLong(0l);

    @Override
    public <T> Event<T> storeEvent(final String stream, final String type, final T payload) {
        return storeEvent(stream, UUID.randomUUID().toString(), type, payload);
    }

    @Override
    public <T> Event<T> storeEvent(final String stream, final String streamId, final String type, final T payload) {
        final Event<T> event = Event.<T> builder()
                .type(type)
                .sequenceId(this.eventIdSequence.incrementAndGet())
                .timestamp(System.currentTimeMillis())
                .stream(stream)
                .streamId(streamId)
                .payload(payload)
                .build();
        this.store.add(event);
        return event;
    }

    @Override
    public List<Event<?>> getEventsByStream(final String stream) {
        return this.store
                .stream()
                .filter(event -> event.getStream().equals(stream))
                .collect(Collectors.toList());
    }

    @Override
    public List<Event<?>> getEvents() {
        throw new UnsupportedOperationException("InMemoryEventStore#getEvents not implemented");
    }

}
