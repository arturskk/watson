package net.lipecki.watson.shop;

import lombok.Builder;
import lombok.Data;
import net.lipecki.watson.event.EventPayload;

import java.util.Optional;

@Data
@Builder
public class ShopAdded implements EventPayload {

    private String name;
    private String retailChainUuid;

    public Optional<String> getRetailChainUuidOptional() {
        return Optional.ofNullable(retailChainUuid);
    }

}
