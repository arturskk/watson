package net.lipecki.watson.combiner;

import net.lipecki.watson.WatsonException;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.event.EventPayload;

import java.util.Map;

public interface AggregateCombinerHandler<T, P extends EventPayload> {

    Class<P> getPayloadClass();

    default boolean canHandle(Event event) {
        return getPayloadClass().equals(event.getPayload().getClass());
    }

    /**
     * Programmatically checks if event is compatible with handler and calls accept.
     *
     * @param collection
     * @param event
     */
    default void acceptWithCheck(Map<String, T> collection, Event event) {
        if (canHandle(event)) {
            accept(collection, event, event.castPayload(getPayloadClass()));
        } else {
            throw WatsonException.of("Handler can't accept incompatible payload objects");
        }
    }

    void accept(Map<String, T> collection, Event event, P payload);

}
