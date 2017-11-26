package net.lipecki.watson.event;

import java.util.List;
import java.util.stream.Stream;

public interface EventStore {

    <T> Event<T> storeEvent(final String stream, final String type, final T payload);

    <T> Event<T> storeEvent(final String stream, final String streamId, final String type, final T payload);

    Stream<Event<?>> getEventsByStream(final List<String> streams);

    Stream<Event<?>> getEvents();

}
