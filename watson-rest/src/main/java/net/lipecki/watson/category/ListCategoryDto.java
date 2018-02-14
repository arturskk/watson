package net.lipecki.watson.category;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ListCategoryDto {

    private int depth;
    private String type;
    private String uuid;
    private String parentUuid;
    private String name;
    private List<String> path;

    public static ListCategoryDto from(final Category category) {
        return ListCategoryDto
                .builder()
                .parentUuid(category.getParent().map(Category::getUuid).orElse(null))
                .type(category.getType())
                .uuid(category.getUuid())
                .name(category.getName())
                .path(category.getCategoryPath())
                .depth(category.getDepth())
                .build();
    }

}
