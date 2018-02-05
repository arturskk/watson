package net.lipecki.watson.projection;

import net.lipecki.watson.event.EventPayload;

public interface ProjectionThread {

    void addHandler(ProjectionHandler<? extends EventPayload> handler);

}
