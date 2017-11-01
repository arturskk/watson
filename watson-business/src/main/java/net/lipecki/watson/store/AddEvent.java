package net.lipecki.watson.store;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddEvent {

    /**
     * Event stream type.
     */
    private String type;

    /**
     * Event payload object.
     *
     * Will be serialized to store specific format, eg. JSON.
     */
    private Object payload;

    public <T> T castPayload(final Class<T> clazz) {
        return clazz.cast(this.payload);
    }

}
