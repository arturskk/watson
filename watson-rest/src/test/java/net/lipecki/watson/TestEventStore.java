package net.lipecki.watson;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.event.EventPayload;
import net.lipecki.watson.event.EventStore;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Test event event with in-memory events.
 *
 * We use thread local to separate each thread and expose reset method to clear event before each test method.
 */
@Slf4j
@Service
public class TestEventStore implements EventStore {

    private ThreadLocal<List<Event>> localEventStore = ThreadLocal.withInitial(ArrayList::new);

    @Override
    public Event storeEvent(final String stream, final EventPayload payload) {
        return storeEvent(stream, UUID.randomUUID(), payload);
    }

    @Override
    public Event storeEvent(final String stream, final UUID streamId, final EventPayload payload) {
        final Event event = Event.builder()
                .type(payload.getClass().getTypeName())
                .timestamp(System.currentTimeMillis())
                .stream(stream)
                .streamId(streamId.toString())
                .payload(payload)
                .build();
        log.debug("Storing event [event={}]", event);
        this.localEventStore.get().add(event);
        return event;
    }


    @Override
    public Stream<Event> getEventsByStream(List<String> streams) {
        throw new UnsupportedOperationException("TestEventStore#getEventsByStream not implemented!");
    }

    @Override
    public Stream<Event> getEvents() {
        throw new UnsupportedOperationException("TestEventStore#getEvents not implemented");
    }


    public void reset() {
        this.localEventStore.remove();
    }

    public List<Event> getAllEvents() {
        return this.localEventStore.get();
    }

    public List<Event> getEvents(final String streamId) {
        return this.localEventStore
                .get()
                .stream()
                .filter(event -> event.getStreamId().equals(streamId))
                .collect(Collectors.toList());
    }

}
