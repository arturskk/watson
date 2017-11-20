package net.lipecki.watson.shop;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class AddShopDto {

    @NotNull
    private String name;

}
