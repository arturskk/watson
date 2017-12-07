package net.lipecki.watson.event;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public class InMemoryEventStore implements EventStore {

    private List<Event<?>> store = new ArrayList<>();
    private AtomicLong eventIdSequence = new AtomicLong(0L);

    @Override
    public <T> Event<T> storeEvent(final String stream, final T payload) {
        return storeEvent(stream, UUID.randomUUID(), payload);
    }

    @Override
    public <T> Event<T> storeEvent(final String stream, final UUID streamId, final T payload) {
        final Event<T> event = Event.<T> builder()
                .type(payload.getClass().getTypeName())
                .sequenceId(this.eventIdSequence.incrementAndGet())
                .timestamp(System.currentTimeMillis())
                .stream(stream)
                .streamId(streamId.toString())
                .payload(payload)
                .build();
        this.store.add(event);
        return event;
    }

    @Override
    public Stream<Event<?>> getEventsByStream(final List<String> streams) {
        return this.store
                .stream()
                .filter(event -> streams.contains(event.getStream()));
    }

    @Override
    public Stream<Event<?>> getEvents() {
        throw new UnsupportedOperationException("InMemoryEventStore#getEvents not implemented");
    }

}
