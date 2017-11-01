package net.lipecki.watson.store;

public interface EventStore {

    String storeEvent(final AddEvent addEvent);

}
