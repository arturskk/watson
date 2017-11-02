package net.lipecki.watson.store;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Event<T> {

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
    private String aggregateId;

    /**
     * Event timestamp.
     */
    private long timestamp;

    /**
     * Event payload object.
     *
     * Will be serialized to store specific format, eg. JSON.
     */
    private T payload;

    public <R> R castPayload(final Class<R> clazz) {
        return clazz.cast(this.payload);
    }

}
