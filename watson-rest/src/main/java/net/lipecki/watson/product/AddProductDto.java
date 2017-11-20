package net.lipecki.watson.product;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class AddProductDto {

    @NotNull
    private String name;
    private String categoryUuid;

}
