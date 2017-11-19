package net.lipecki.watson.combiner;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.WatsonException;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.event.EventStore;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Utility class for in-memory aggregates.
 *
 * Allows for in-memory parsing all stream events with provided event handlers.
 *
 * @param <T>
 */
@Slf4j
public class AggregateStreamCombiner<T> {

    private final EventStore eventStore;
    private final String stream;
    private final Map<String, BiConsumer<Map<String, T>, Event<?>>> handlerMapping = new HashMap<>();
    private boolean ignoreMissingEventTypes = false;

    public AggregateStreamCombiner(final EventStore eventStore, final String stream) {
        this.eventStore = eventStore;
        this.stream = stream;
    }

    public void registerHandler(final String eventType, final BiConsumer<Map<String, T>, Event<?>> handler) {
        this.handlerMapping.put(eventType, handler);
    }

    public void setIgnoreMissingEventTypes(final boolean ignoreMissingEventTypes) {
        this.ignoreMissingEventTypes = ignoreMissingEventTypes;
    }

    public List<T> getAsList() {
        return new ArrayList<>(get().values());
    }

    public Map<String, T> get(final Supplier<Map<String, T>> stateInitializer) {
        final List<Event<?>> events = this.eventStore
                .getEventsByStream(this.stream)
                .stream()
                .sorted(Comparator.comparing(Event::getSequenceId))
                .collect(Collectors.toList());

        final Map<String, T> result = stateInitializer.get();

        for (final Event<?> event : events) {
            final String eventType = event.getType();
            if (this.handlerMapping.containsKey(eventType)) {
                final BiConsumer<Map<String, T>, Event<?>> eventHandler = this.handlerMapping.get(eventType);
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

    public Map<String, T> get() {
        return get(() -> new HashMap<>());
    }

}
