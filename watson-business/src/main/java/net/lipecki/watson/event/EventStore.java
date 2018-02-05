package net.lipecki.watson.event;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public interface EventStore {

    Event storeEvent(final String stream, final EventPayload payload);

    Event storeEvent(final String stream, final UUID streamId, final EventPayload payload);

    long getLastSequenceId();

    Stream<Event> getEventsByStream(final List<String> streams);

    Stream<Event> getEvents();

    Stream<Event> getEventsAfter(final long sequenceId, final int limit);

}
