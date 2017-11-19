package net.lipecki.watson.category;

import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
@Builder
public class AddCategory {

    private String type;
    private String name;
    private String parentUuid;

    public Optional<String> getParentUuidOptional() {
        return Optional.ofNullable(parentUuid);
    }

}
