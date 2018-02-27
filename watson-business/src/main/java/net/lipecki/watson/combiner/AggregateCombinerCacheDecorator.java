package net.lipecki.watson.combiner;

import net.lipecki.watson.event.EventPayload;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AggregateCombinerCacheDecorator<T> implements AggregateCombiner<T> {

    private final Cache cache;
    private String cacheKey;
    private final List<String> streams;
    private AggregateCombiner<T> delegate;

    public AggregateCombinerCacheDecorator(
            final String cacheKey,
            final CacheManager cacheManager,
            final List<String> streams,
            final AggregateCombiner<T> delegate) {
        this.cacheKey = cacheKey;
        this.streams = streams;
        this.delegate = delegate;
        this.cache = cacheManager.getCache("AggregateCombinerCacheDecorator");
    }

    @Override
    public List<T> getAsList() {
        return new ArrayList<>(get().values());
    }

    @Override
    public Map<String, T> get() {
        return cache.get(cacheKey, () -> delegate.get());
    }

    @Override
    public void addHandler(final AggregateCombinerHandler<T, ? extends EventPayload> handler) {
        delegate.addHandler(handler);
    }

    @Override
    public void setIgnoreUnhandledEvents(final boolean ignoreUnhandledEvents) {
        delegate.setIgnoreUnhandledEvents(ignoreUnhandledEvents);
    }

}
