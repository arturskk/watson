package net.lipecki.watson.shop;

import lombok.Builder;
import lombok.Data;
import net.lipecki.watson.event.EventPayload;

@Data
@Builder
public class ShopAdded implements EventPayload {

    private String name;

}
