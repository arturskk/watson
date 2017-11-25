package net.lipecki.watson.category;

import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
@Builder
public class ModifyCategory {

    private String type;
    private String uuid;
    private String name;
    private String parentUuid;

    public Optional<String> getParentUuidOptional() {
        return Optional.ofNullable(parentUuid);
    }

    public Optional<String> getNameOptional() {
        return Optional.ofNullable(name);
    }

}
