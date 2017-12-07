package net.lipecki.watson.combiner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface AggregateCombiner<T> {

    default List<T> getAsList() {
        return new ArrayList<>(get().values());
    }

    Map<String, T> get();

    void addHandler(final Class<?> eventClass, final AggregateCombinerHandler<T> handler);

    void setIgnoreMissingEventTypes(final boolean ignoreMissingEventTypes);

}
