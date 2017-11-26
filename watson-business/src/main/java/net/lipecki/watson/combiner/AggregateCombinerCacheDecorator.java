package net.lipecki.watson.combiner;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AggregateCombinerCacheDecorator<T> implements AggregateCombiner<T> {

    private final Cache cache;
    private final List<String> streams;
    private AggregateCombiner<T> delegate;

    public AggregateCombinerCacheDecorator(final CacheManager cacheManager, final List<String> streams, final AggregateCombiner<T> delegate) {
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
        return cache.get(getCacheKey(), () -> delegate.get());
    }

    @Override
    public void addHandler(final String eventType, final AggregateCombinerHandler<T> handler) {
        delegate.addHandler(eventType, handler);
    }

    @Override
    public void setIgnoreMissingEventTypes(final boolean ignoreMissingEventTypes) {
        delegate.setIgnoreMissingEventTypes(ignoreMissingEventTypes);
    }

    private String getCacheKey() {
        return this.streams.stream().collect(Collectors.joining("+"));
    }

}
