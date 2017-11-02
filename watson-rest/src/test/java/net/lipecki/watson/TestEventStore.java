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

    private ThreadLocal<Map<String, List<Event>>> localEventStore = ThreadLocal.withInitial(() -> new HashMap<>());

    @Override
    public String storeEvent(Event event) {
        log.debug("New event to store [event={}]", event);
        final String streamId = UUID.randomUUID().toString();
        this.localEventStore.get().getOrDefault(streamId, new ArrayList<>()).add(event);
        return streamId;
    }

    public void reset() {
        this.localEventStore.remove();
    }

    public Map<String, List<Event>> getAllEvents() {
        return this.localEventStore.get();
    }

    public List<Event> getEvents(final String streamId) {
        return this.localEventStore.get().getOrDefault(streamId, new ArrayList<>());
    }

}
