package net.lipecki.watson.combiner;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.WatsonException;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.event.EventPayload;
import net.lipecki.watson.event.EventStore;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Utility class for in-memory aggregates.
 * <p>
 * Allows for in-memory parsing all streams events with provided event handlers.
 *
 * @param <T>
 */
@Slf4j
public class InMemoryAggregateCombiner<T> implements AggregateCombiner<T> {

    private final EventStore eventStore;
    private final List<String> streams;
    private final Supplier<Map<String, T>> stateInitializer;
    private final Map<String, List<AggregateCombinerHandler<T, ? extends EventPayload>>> handlerMapping = new HashMap<>();
    private boolean ignoreUnhandledEvents = false;

    InMemoryAggregateCombiner(final EventStore eventStore, final List<String> streams) {
        this(eventStore, streams, HashMap::new);
    }

    InMemoryAggregateCombiner(final EventStore eventStore, final List<String> streams, final Supplier<Map<String, T>> stateInitializer) {
        this.eventStore = eventStore;
        this.streams = streams;
        this.stateInitializer = stateInitializer;
    }

    @Override
    public void addHandler(final AggregateCombinerHandler<T, ? extends EventPayload> handler) {
        final String typeName = handler.getPayloadClass().getTypeName();
        this.handlerMapping.putIfAbsent(typeName, new ArrayList<>());
        this.handlerMapping.get(typeName).add(handler);
    }

    @Override
    public void setIgnoreUnhandledEvents(final boolean ignoreUnhandledEvents) {
        this.ignoreUnhandledEvents = ignoreUnhandledEvents;
    }

    @Override
    public Map<String, T> get() {
        log.debug("Combining streams [streams={}]", this.streams);

        final List<Event> events = this.eventStore
                .getEventsByStream(this.streams)
                .sorted(Comparator.comparing(Event::getSequenceId))
                .collect(Collectors.toList());
        log.trace("Events selected to combine for streams [streams={}, eventsCount={}]", this.streams, events.size());

        final Map<String, T> result = this.stateInitializer.get();

        for (final Event event : events) {
            final String eventType = event.getType();
            final List<AggregateCombinerHandler<T, ? extends EventPayload>> eventHandlers = this.handlerMapping.getOrDefault(eventType, new ArrayList<>());
            boolean atLeastOneHandler = false;
            for (AggregateCombinerHandler<T, ? extends EventPayload> handler : eventHandlers) {
                if (handler.canHandle(event)) {
                    try {
                        atLeastOneHandler = true;
                        handler.acceptWithCheck(result, event);
                    } catch (final Exception ex) {
                        log.warn(
                                "Event processing skipped due to handler exception [event={}]",
                                event,
                                WatsonException
                                        .of("Exception while handling event", ex)
                                        .with("event", event)
                                        .with("handler", handler.getClass().getSimpleName())
                        );
                    }
                }
            }
            if (!atLeastOneHandler && !ignoreUnhandledEvents) {
                throw WatsonException
                        .of("Missing event handler for stream combiner")
                        .with("streams", this.streams)
                        .with("missingEventType", eventType);
            }
        }

        return result;
    }

}
