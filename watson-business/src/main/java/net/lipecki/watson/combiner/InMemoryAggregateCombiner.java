package net.lipecki.watson.combiner;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.WatsonException;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.event.EventStore;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Utility class for in-memory aggregates.
 * <p>
 * Allows for in-memory parsing all stream events with provided event handlers.
 *
 * @param <T>
 */
@Slf4j
public class InMemoryAggregateCombiner<T> implements AggregateCombiner<T> {

    private final EventStore eventStore;
    private final String stream;
    private final Supplier<Map<String, T>> stateInitializer;
    private final Map<String, AggregateCombinerHandler<T>> handlerMapping = new HashMap<>();
    private boolean ignoreMissingEventTypes = false;

    InMemoryAggregateCombiner(final EventStore eventStore, final String stream) {
        this(eventStore, stream, HashMap::new);
    }

    InMemoryAggregateCombiner(final EventStore eventStore, final String stream, final Supplier<Map<String, T>> stateInitializer) {
        this.eventStore = eventStore;
        this.stream = stream;
        this.stateInitializer = stateInitializer;
    }

    @Override
    public void addHandler(final String eventType, final AggregateCombinerHandler<T> handler) {
        this.handlerMapping.put(eventType, handler);
    }

    @Override
    public void setIgnoreMissingEventTypes(final boolean ignoreMissingEventTypes) {
        this.ignoreMissingEventTypes = ignoreMissingEventTypes;
    }

    @Override
    public Map<String, T> get() {
        log.debug("Combining stream [stream={}]", this.stream);

        final List<Event<?>> events = this.eventStore
                .getEventsByStream(this.stream)
                .sorted(Comparator.comparing(Event::getSequenceId))
                .collect(Collectors.toList());
        log.trace("Events selected to combine for stream [stream={}, eventsCount={}]", this.stream, events.size());

        final Map<String, T> result = this.stateInitializer.get();

        for (final Event<?> event : events) {
            final String eventType = event.getType();
            if (this.handlerMapping.containsKey(eventType)) {
                final AggregateCombinerHandler<T> eventHandler = this.handlerMapping.get(eventType);
                try {
                    eventHandler.accept(result, event);
                } catch (final Exception ex) {
                    log.warn("Event processing skipped due to handler exception [event={}]", event, ex);
                }
            } else if (!this.ignoreMissingEventTypes) {
                throw WatsonException
                        .of("Missing event handler for stream combiner")
                        .with("stream", this.stream)
                        .with("missingEventType", eventType);
            }
        }

        return result;
    }

}
