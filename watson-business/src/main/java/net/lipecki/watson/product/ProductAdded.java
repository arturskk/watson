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
    private String defaultUnit;
    private String producerUuid;

    public Optional<String> getCategoryUuidOptional() {
        return Optional.ofNullable(categoryUuid);
    }

    public Optional<String> getDefaultUnitOptional() { return Optional.ofNullable(defaultUnit); }

    public Optional<String> getProducerUuidOptional() { return Optional.ofNullable(producerUuid); }

}
