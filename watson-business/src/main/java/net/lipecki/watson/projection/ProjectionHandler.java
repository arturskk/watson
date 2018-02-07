package net.lipecki.watson.projection;

import net.lipecki.watson.WatsonException;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.event.EventPayload;

public interface ProjectionHandler<P extends EventPayload> {

    Class<P> getPayloadClass();

    default boolean canHandle(Event event) {
        return getPayloadClass().equals(event.getPayload().getClass());
    }

    /**
     * Programmatically checks if event is compatible with handler and calls accept.
     *
     * @param event
     */
    default void acceptWithCheck(final Event event) {
        if (canHandle(event)) {
            accept(event, event.castPayload(getPayloadClass()));
        } else {
            throw WatsonException.of("Handler can't accept incompatible payload objects");
        }
    }

    void accept(final Event event, P payload);

}
