package net.lipecki.watson.store;

import java.util.List;

public interface EventStore {

    <T> Event<T> storeEvent(final String stream, final String type, final T payload);

    <T> Event<T> storeEvent(final String stream, final String streamId, final String type, final T payload);

    List<Event<?>> getEventsByStream(final String stream);

}
