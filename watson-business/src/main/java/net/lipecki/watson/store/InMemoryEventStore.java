package net.lipecki.watson.store;

import java.util.*;

public class InMemoryEventStore implements EventStore {

    private Map<String, List<Event<?>>> store = new HashMap<>();

    @Override
    public <T> Event<T> storeEvent(final String type, final T payload) {
        return storeEvent(UUID.randomUUID().toString(), type, payload);
    }

    @Override
    public <T> Event<T> storeEvent(final String streamId, final String type, final T payload) {
        final Event<T> event = Event.<T>builder()
                .type(type)
                .timestamp(System.currentTimeMillis())
                .streamId(streamId)
                .payload(payload)
                .build();
        this.store.getOrDefault(streamId, new ArrayList<>()).add(event);
        return event;
    }

}
