package net.lipecki.watson;

import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString(of = {"code", "message", "data"})
public class WatsonException extends RuntimeException {

    private WatsonExceptionCode code;
    private Map<String, Object> data = new HashMap<>();

    public static WatsonException of(final String message) {
        return new WatsonException(WatsonExceptionCode.UNKNOWN, message);
    }

    public static WatsonException of(final WatsonExceptionCode code, final String message, final Exception cause) {
        return new WatsonException(code, message, cause);
    }

    public WatsonException(final WatsonExceptionCode code, final String message, final Exception cause) {
        super(message, cause);
        this.code = code;
    }

    public WatsonException(final WatsonExceptionCode code, final String message) {
        super(message);
        this.code = code;
    }

    public WatsonException with(final String key, final Object value) {
        this.data.put(key, value);
        return this;
    }

}
