package net.lipecki.watson.product;

import lombok.Builder;
import lombok.Data;
import net.lipecki.watson.event.EventPayload;

import java.util.Optional;

@Data
@Builder
public class ProductAdded implements EventPayload {

    private String name;
    private String categoryUuid;

    public Optional<String> getCategoryUuidOptional() {
        return Optional.ofNullable(categoryUuid);
    }

}
