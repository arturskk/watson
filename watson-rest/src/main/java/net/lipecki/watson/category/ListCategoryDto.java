package net.lipecki.watson.category;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ListCategoryDto {

    private String type;
    private String uuid;
    private String parentUuid;
    private String name;
    private String path;

}
