package net.lipecki.watson.projection;

import net.lipecki.watson.event.Event;
import net.lipecki.watson.event.EventPayload;

public interface ProjectionThread {

    void addHandler(ProjectionHandler<? extends EventPayload> handler);
    void setId(String id);
    void start();
    void pushEvent(final Event event);
    void resetProjection(final Runnable resetCallback);
    ProjectionStatus getStatus();

}
