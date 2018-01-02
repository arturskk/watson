package net.lipecki.watson;

import lombok.ToString;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@ToString(of = {"code", "message", "data"})
public class WatsonException extends RuntimeException {

    private final WatsonExceptionCode code;
    private final Map<String, Object> data = new HashMap<>();

    public static Supplier<WatsonException> supplier(final String message) {
        return () -> WatsonException.of(message);
    }

    public static WatsonException of(final String message) {
        return new WatsonException(WatsonExceptionCode.UNKNOWN, message);
    }

    public static WatsonException of(final String message, final Throwable cause) {
        return new WatsonException(WatsonExceptionCode.UNKNOWN, message, cause);
    }

    public static WatsonException of(final WatsonExceptionCode code, final String message) {
        return new WatsonException(code, message);
    }

    public static WatsonException of(final WatsonExceptionCode code, final String message, final Throwable cause) {
        return new WatsonException(code, message, cause);
    }

    public WatsonException(final WatsonExceptionCode code, final String message, final Throwable cause) {
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

    @Override
    public String getMessage() {
        return String.format(
                "%s [%s]",
                super.getMessage(),
                this.data
        );
    }
}
