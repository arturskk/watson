package net.lipecki.watson.reatialchain;

import lombok.Builder;
import lombok.Data;
import net.lipecki.watson.event.EventPayload;

import java.util.Optional;

@Data
@Builder
public class RetailChainAdded implements EventPayload {

    private String name;

}
