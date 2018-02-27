package net.lipecki.watson.combiner;

import net.lipecki.watson.event.EventStore;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class TestAggregateCombinerWithCacheFactory implements AggregateCombinerFactory {

    private final EventStore eventStore;

    public static AggregateCombinerFactory of(final EventStore eventStore) {
        return new TestAggregateCombinerWithCacheFactory(eventStore);
    }

    public TestAggregateCombinerWithCacheFactory(final EventStore eventStore) {
        this.eventStore = eventStore;
    }

    @Override
    public <T> AggregateCombiner<T> getAggregateCombiner(final String cacheKey, final List<String> streams) {
        return new InMemoryAggregateCombiner<>(eventStore, streams);
    }

    @Override
    public <T> AggregateCombiner<T> getAggregateCombiner(final String cacheKey, final List<String> streams, final Supplier<Map<String, T>> initializer) {
        return new InMemoryAggregateCombiner<>(eventStore, streams, initializer);
    }

}
