package net.lipecki.watson.product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ListProductDto {

    private String uuid;
    private String name;
    private String categoryUuid;
    private String categoryName;
    private String categoryPath;

}
