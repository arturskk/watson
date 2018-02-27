package net.lipecki.watson.reatialchain;

import lombok.Builder;
import lombok.Data;
import net.lipecki.watson.event.EventPayload;

@Data
@Builder
public class RetailChainAdded implements EventPayload {

    private String name;

}
