package net.lipecki.watson.category;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Data
@Builder
public class AddCategoryDto {

    @NotNull
    private String type;
    @NotNull
    private String name;
    private Optional<String> parentUuid;

}
