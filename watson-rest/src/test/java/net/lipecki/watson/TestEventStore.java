package net.lipecki.watson;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.store.AddEvent;
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

    private ThreadLocal<Map<String, List<AddEvent>>> localEventStore = ThreadLocal.withInitial(() -> new HashMap<>());

    @Override
    public String storeEvent(AddEvent addEvent) {
        log.debug("New event to store [event={}]", addEvent);
        final String streamId = UUID.randomUUID().toString();
        this.localEventStore.get().getOrDefault(streamId, new ArrayList<>()).add(addEvent);
        return streamId;
    }

    public void reset() {
        this.localEventStore.remove();
    }

    public Map<String, List<AddEvent>> getAllEvents() {
        return this.localEventStore.get();
    }

    public List<AddEvent> getEvents(final String streamId) {
        return this.localEventStore.get().getOrDefault(streamId, new ArrayList<>());
    }

}
