package net.lipecki.watson.category;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModifyCategoryData {

    private String type;
    private String uuid;
    private String name;
    private String parentUuid;

}
