package net.lipecki.watson.category;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ListCategoryDto {

    public static ListCategoryDto from(final Category category) {
        return ListCategoryDto
                .builder()
                .parentUuid(category.getParent().map(Category::getUuid).orElse(null))
                .type(category.getType())
                .uuid(category.getUuid())
                .name(category.getName())
                .pathString(category.getCategoryPath())
                .build();
    }

    private String type;
    private String uuid;
    private String parentUuid;
    private String name;
    private String pathString;

}
