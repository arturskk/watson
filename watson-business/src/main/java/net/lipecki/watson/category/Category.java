package net.lipecki.watson.category;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Category {

    public static final String CATEGORY_STREAM = "_category";

    private String uuid;
    private String type;
    private String name;
    private String categoryPath;
    private Category parent;

}
