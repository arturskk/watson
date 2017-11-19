package net.lipecki.watson.product;

import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
@Builder
public class AddProduct {

    private String name;
    private String categoryUuid;

    public Optional<String> getCategoryUuidOptional() {
        return Optional.ofNullable(categoryUuid);
    }

}
