package net.lipecki.watson.product;

import lombok.Builder;
import lombok.Data;
import net.lipecki.watson.event.EventPayload;

import java.util.Optional;

@Data
@Builder
public class ProductModified implements EventPayload {

    private String uuid;
    private String name;
    private String categoryUuid;
    private String defaultUnit;

    public Optional<String> getCategoryUuidOptional() {
        return Optional.ofNullable(categoryUuid);
    }

    public Optional<String> getNameOptional() {
        return Optional.ofNullable(name);
    }

    public Optional<String> getDefaultUnitOptional() { return Optional.ofNullable(defaultUnit); }

}
