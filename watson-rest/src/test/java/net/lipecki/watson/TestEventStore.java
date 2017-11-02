package net.lipecki.watson;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.store.Event;
import net.lipecki.watson.store.EventStore;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Test event store with in-memory events.
 *
 * We use thread local to separate each thread and expose reset method to clear store before each test method.
 */
@Slf4j
@Service
public class TestEventStore implements EventStore {

    private ThreadLocal<Map<String, List<Event<?>>>> localEventStore = ThreadLocal.withInitial(() -> new HashMap<>());

    @Override
    public <T> Event<T> storeEvent(final String type, final T payload) {
        return storeEvent(UUID.randomUUID().toString(), type, payload);
    }

    @Override
    public <T> Event<T> storeEvent(final String streamId, final String type, final T payload) {
        final Event<T> event = Event.<T>builder()
                .type(type)
                .timestamp(System.currentTimeMillis())
                .streamId(streamId)
                .payload(payload)
                .build();
        this.localEventStore.get().getOrDefault(streamId, new ArrayList<>()).add(event);
        return event;
    }

    public void reset() {
        this.localEventStore.remove();
    }

    public Map<String, List<Event<?>>> getAllEvents() {
        return this.localEventStore.get();
    }

    public List<Event<?>> getEvents(final String streamId) {
        return this.localEventStore.get().getOrDefault(streamId, new ArrayList<>());
    }

}
