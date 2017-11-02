package net.lipecki.watson.store;

public interface EventStore {

    String storeEvent(final Event<?> event);

}
