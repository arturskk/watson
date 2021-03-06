package net.lipecki.watson.account;

import lombok.Builder;
import lombok.Data;
import net.lipecki.watson.event.EventPayload;

import java.util.Optional;

@Data
@Builder
public class AccountAdded implements EventPayload {

    private String name;
    private Boolean useDefault;

    public Optional<Boolean> getUseDefaultOptional() {
        return Optional.ofNullable(useDefault);
    }

}
