package net.lipecki.watson.store;

import java.util.*;

public class InMemoryEventStore implements EventStore {

    private Map<String, List<Event>> store = new HashMap<>();

    @Override
    public String storeEvent(Event event) {
        final String streamId = UUID.randomUUID().toString();
        this.store.getOrDefault(streamId, new ArrayList<>()).add(event);
        return streamId;
    }

}
