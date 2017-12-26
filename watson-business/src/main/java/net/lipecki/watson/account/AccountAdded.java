package net.lipecki.watson.account;

import lombok.Builder;
import lombok.Data;
import net.lipecki.watson.event.EventPayload;

@Data
@Builder
public class AccountAdded implements EventPayload {

    private String name;

}
