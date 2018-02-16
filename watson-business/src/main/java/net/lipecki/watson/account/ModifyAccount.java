package net.lipecki.watson.account;

import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
@Builder
public class ModifyAccount {

    private String uuid;
    private String name;

    public Optional<String> getNameOptional() {
        return Optional.ofNullable(name);
    }

}
