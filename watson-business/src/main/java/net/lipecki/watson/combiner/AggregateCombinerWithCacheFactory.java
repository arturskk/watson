package net.lipecki.watson.combiner;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.event.EventStore;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Slf4j
@Service
public class AggregateCombinerWithCacheFactory implements AggregateCombinerFactory {

    private final EventStore eventStore;
    private final CacheManager cacheManager;

    public AggregateCombinerWithCacheFactory(final EventStore eventStore, final CacheManager cacheManager) {
        this.eventStore = eventStore;
        this.cacheManager = cacheManager;
    }

    @Override
    public <T> AggregateCombiner<T> getAggregateCombiner(final String stream) {
        return new AggregateCombinerCacheDecorator<>(
                this.cacheManager,
                stream,
                new InMemoryAggregateCombiner<>(
                        this.eventStore,
                        stream,
                        HashMap<String, T>::new
                )
        );
    }

    @Override
    public <T> AggregateCombiner<T> getAggregateCombiner(final String stream, final Supplier<Map<String, T>> initializer) {
        return new AggregateCombinerCacheDecorator<>(
                this.cacheManager,
                stream,
                new InMemoryAggregateCombiner<>(
                        this.eventStore,
                        stream,
                        initializer
                )
        );
    }

}
