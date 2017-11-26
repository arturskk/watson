package net.lipecki.watson.combiner;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AggregateCombinerCacheDecorator<T> implements AggregateCombiner<T> {

    private final Cache cache;
    private final String stream;
    private AggregateCombiner<T> delegate;

    public AggregateCombinerCacheDecorator(final CacheManager cacheManager, final String stream, final AggregateCombiner<T> delegate) {
        this.stream = stream;
        this.delegate = delegate;
        this.cache = cacheManager.getCache("AggregateCombinerCacheDecorator");
    }

    @Override
    public List<T> getAsList() {
        return new ArrayList<>(get().values());
    }

    @Override
    public Map<String, T> get() {
        return cache.get(this.stream, () -> delegate.get());
    }

    @Override
    public void addHandler(final String eventType, final AggregateCombinerHandler<T> handler) {
        delegate.addHandler(eventType, handler);
    }

    @Override
    public void setIgnoreMissingEventTypes(final boolean ignoreMissingEventTypes) {
        delegate.setIgnoreMissingEventTypes(ignoreMissingEventTypes);
    }

}
