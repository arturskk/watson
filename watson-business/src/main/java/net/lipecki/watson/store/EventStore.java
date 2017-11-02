package net.lipecki.watson.store;

public interface EventStore {

    <T> Event<T> storeEvent(final String type, final T payload);

    <T> Event<T> storeEvent(final String streamId, final String type, final T payload);

}
