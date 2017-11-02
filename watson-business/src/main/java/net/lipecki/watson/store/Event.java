package net.lipecki.watson.store;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Event<T> {

    /**
     * Event stream type.
     */
    private String type;

    /**
     * Stream id (eg. aggregate id).
     */
    private String streamId;

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

}
