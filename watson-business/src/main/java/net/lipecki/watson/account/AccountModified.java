package net.lipecki.watson.account;

import lombok.Builder;
import lombok.Data;
import net.lipecki.watson.event.EventPayload;

import java.util.Optional;

@Data
@Builder
public class AccountModified implements EventPayload {

    private String uuid;
    private String name;
    private Boolean useDefault;

    public Optional<String> getNameOptional() {
        return Optional.ofNullable(name);
    }

    public Optional<Boolean> getUseDefaultOptional() {
        return Optional.ofNullable(useDefault);
    }

}
