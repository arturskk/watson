package net.lipecki.watson.category;

import lombok.Builder;
import lombok.Data;
import net.lipecki.watson.event.EventPayload;

import java.util.Optional;

@Data
@Builder
public class CategoryModified implements EventPayload {

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
