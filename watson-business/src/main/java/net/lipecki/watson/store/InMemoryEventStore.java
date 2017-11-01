package net.lipecki.watson.store;

import java.util.*;

public class InMemoryEventStore implements EventStore {

    private Map<String, List<AddEvent>> store = new HashMap<>();

    @Override
    public String storeEvent(AddEvent addEvent) {
        final String streamId = UUID.randomUUID().toString();
        this.store.getOrDefault(streamId, new ArrayList<>()).add(addEvent);
        return streamId;
    }

}
