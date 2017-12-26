package net.lipecki.watson.event;

public interface EventPayload {

    default long getSchemaVersion() {
        return 1;
    }

}
