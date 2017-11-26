package net.lipecki.watson.event;

import org.springframework.context.ApplicationEventPublisher;

import java.util.stream.Stream;

public class PublishingEventStoreDecorator implements EventStore {

    private final EventStore eventStore;
    private final ApplicationEventPublisher eventPublisher;

    public PublishingEventStoreDecorator(final EventStore eventStore, final ApplicationEventPublisher eventPublisher) {
        this.eventStore = eventStore;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public <T> Event<T> storeEvent(final String stream, final String type, final T payload) {
        final Event<T> event = eventStore.storeEvent(stream, type, payload);
        this.eventPublisher.publishEvent(event);
        return event;
    }

    @Override
    public <T> Event<T> storeEvent(final String stream, final String streamId, final String type, final T payload) {
        final Event<T> event = eventStore.storeEvent(stream, streamId, type, payload);
        this.eventPublisher.publishEvent(event);
        return event;
    }

    @Override
    public Stream<Event<?>> getEventsByStream(final String stream) {
        return eventStore.getEventsByStream(stream);
    }

    @Override
    public Stream<Event<?>> getEvents() {
        return eventStore.getEvents();
    }

}
