package net.lipecki.watson.product;

import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
@Builder
public class ModifyProduct {

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

}
