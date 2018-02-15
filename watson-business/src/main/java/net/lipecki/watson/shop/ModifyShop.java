package net.lipecki.watson.shop;

import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
@Builder
public class ModifyShop {

    private String uuid;
    private String name;

    public Optional<String> getNameOptional() {
        return Optional.ofNullable(name);
    }

}
