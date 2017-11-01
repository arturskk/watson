package net.lipecki.watson.category;

import lombok.Data;

@Data
public class Category {

    public static final String STREAM_TYPE = "_category";

    private String type;
    private String name;
    private String categoryPath;
    private String uuid;
    private Category parent;

}
