package net.lipecki.watson.event;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class Event {

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
     * Event date.
     */
    private long timestamp;

    /**
     * Event payload object.
     *
     * Will be serialized to event specific format, eg. JSON.
     */
    private EventPayload payload;

    public <R extends EventPayload> R castPayload(final Class<R> clazz) {
        return clazz.cast(this.payload);
    }

    public Map<String, Object> asMap() {
        final Map<String, Object> map = new HashMap<>();
        map.put("sequenceId", getSequenceId());
        map.put("stream", getStream());
        map.put("streamId", getStreamId());
        map.put("type", getType());
        map.put("timestamp", getTimestamp());
        map.put("payload", getPayload());
        map.put("payloadClass", getPayload().getClass().getTypeName());
        return map;
    }

}
