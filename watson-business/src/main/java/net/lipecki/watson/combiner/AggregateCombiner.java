package net.lipecki.watson.combiner;

import net.lipecki.watson.event.EventPayload;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface AggregateCombiner<T> {

    default List<T> getAsList() {
        return new ArrayList<>(get().values());
    }

    Map<String, T> get();

    void addHandler(final AggregateCombinerHandler<T, ? extends EventPayload> handler);

}
