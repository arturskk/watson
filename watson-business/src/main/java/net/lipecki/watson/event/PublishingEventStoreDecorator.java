package net.lipecki.watson.event;

import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class PublishingEventStoreDecorator implements EventStore {

    private final EventStore eventStore;
    private final ApplicationEventPublisher eventPublisher;

    public PublishingEventStoreDecorator(final EventStore eventStore, final ApplicationEventPublisher eventPublisher) {
        this.eventStore = eventStore;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public <T> Event<T> storeEvent(final String stream, final T payload) {
        final Event<T> event = eventStore.storeEvent(stream, payload);
        this.eventPublisher.publishEvent(event);
        return event;
    }

    @Override
    public <T> Event<T> storeEvent(final String stream, final UUID streamId, final T payload) {
        final Event<T> event = eventStore.storeEvent(stream, streamId, payload);
        this.eventPublisher.publishEvent(event);
        return event;
    }

    @Override
    public Stream<Event<?>> getEventsByStream(final List<String> streams) {
        return eventStore.getEventsByStream(streams);
    }

    @Override
    public Stream<Event<?>> getEvents() {
        return eventStore.getEvents();
    }

}
