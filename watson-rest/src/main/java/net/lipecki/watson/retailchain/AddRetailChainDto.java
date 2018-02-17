package net.lipecki.watson.retailchain;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class AddRetailChainDto {

    @NotNull
    private String name;

}
