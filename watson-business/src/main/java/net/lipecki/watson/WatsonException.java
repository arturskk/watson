package net.lipecki.watson;

import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString(of = {"code", "message", "data"})
public class WatsonException extends RuntimeException {

    private WatsonExceptionCode code;
    private Map<String, Object> data = new HashMap<>();

    public WatsonException(final WatsonExceptionCode code, final String message) {
        super(message);
        this.code = code;
    }

    public WatsonException put(final String key, final Object value) {
        this.data.put(key, value);
        return this;
    }

}
