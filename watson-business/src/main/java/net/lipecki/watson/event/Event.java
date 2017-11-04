package net.lipecki.watson.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Event<T> {

    /**
     * System wide unique event sequence id.
     */
    private long sequenceId;

    /**
     * Event type.
     */
    private String type;

    /**
     * Stream.
     */
    private String stream;

    /**
     * Aggregate within stream id.
     */
    private String streamId;

    /**
     * Event timestamp.
     */
    private long timestamp;

    /**
     * Event payload object.
     *
     * Will be serialized to event specific format, eg. JSON.
     */
    private T payload;

    public <R> R castPayload(final Class<R> clazz) {
        return clazz.cast(this.payload);
    }

}
