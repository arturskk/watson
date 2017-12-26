package net.lipecki.watson.event;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public interface EventStore {

    Event storeEvent(final String stream, final EventPayload payload);

    Event storeEvent(final String stream, final UUID streamId, final EventPayload payload);

    Stream<Event> getEventsByStream(final List<String> streams);

    Stream<Event> getEvents();

}
