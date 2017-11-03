package net.lipecki.watson.category;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class AddCategoryDto {

    @NotNull
    private String type;
    @NotNull
    private String name;
    private String parentUuid;

}
