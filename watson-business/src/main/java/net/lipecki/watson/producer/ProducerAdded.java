package net.lipecki.watson.producer;

import lombok.Builder;
import lombok.Data;
import net.lipecki.watson.event.EventPayload;

@Data
@Builder
public class ProducerAdded implements EventPayload {

    private String name;

}
