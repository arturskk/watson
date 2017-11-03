package net.lipecki.watson;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.store.Event;
import net.lipecki.watson.store.EventStore;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Test event store with in-memory events.
 *
 * We use thread local to separate each thread and expose reset method to clear store before each test method.
 */
@Slf4j
@Service
public class TestEventStore implements EventStore {

    private ThreadLocal<List<Event<?>>> localEventStore = ThreadLocal.withInitial(() -> new ArrayList<>());

    @Override
    public <T> Event<T> storeEvent(final String stream, final String type, final T payload) {
        return storeEvent(stream, UUID.randomUUID().toString(), type, payload);
    }

    @Override
    public <T> Event<T> storeEvent(final String stream, final String streamId, final String type, final T payload) {
        final Event<T> event = Event.<T> builder()
                .type(type)
                .timestamp(System.currentTimeMillis())
                .stream(stream)
                .aggregateId(streamId)
                .payload(payload)
                .build();
        log.debug("Storing event [event={}]", event);
        this.localEventStore.get().add(event);
        return event;
    }


    @Override
    public List<Event<?>> getEventsByStream(String stream) {
        throw new UnsupportedOperationException("TestEventStore#getEventsByStream not implemented!");
    }


    public void reset() {
        this.localEventStore.remove();
    }

    public List<Event<?>> getAllEvents() {
        return this.localEventStore.get();
    }

    public List<Event<?>> getEvents(final String streamId) {
        return this.localEventStore
                .get()
                .stream()
                .filter(event -> event.getAggregateId().equals(streamId))
                .collect(Collectors.toList());
    }

}
