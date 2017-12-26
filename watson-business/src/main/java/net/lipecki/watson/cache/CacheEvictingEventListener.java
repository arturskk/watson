package net.lipecki.watson.cache;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.event.EventPayload;
import org.springframework.cache.CacheManager;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CacheEvictingEventListener {

    private final CacheManager cacheManager;

    public CacheEvictingEventListener(final CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @EventListener
    public <T extends EventPayload> void handleEventStored(final Event event) {
        log.info("Evicting all caches after new event stored [stream={}, type={}]", event.getStream(), event.getType());
        this.cacheManager
                .getCacheNames()
                .parallelStream()
                .map(this.cacheManager::getCache)
                .forEach(cache -> cache.clear());
    }

}
