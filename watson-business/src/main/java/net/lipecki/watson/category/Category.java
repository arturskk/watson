package net.lipecki.watson.category;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@EqualsAndHashCode(of = "uuid")
@Data
@Builder
public class Category {

    public static String combineCategoryPath(final String categoryName, final Category parent) {
        return parent.getCategoryPath() + Category.PATH_SEPARATOR + categoryName;
    }

    public static final String CATEGORY_STREAM = "_category";
    public static final String ROOT_UUID = "root";
    public static final String ROOT_NAME_KEY = ":root-category";
    public static final String PATH_SEPARATOR = " > ";

    private String uuid;
    private String type;
    private String name;
    private String categoryPath;
    private Category parent;
    @SuppressWarnings("UnusedAssignment")
    @Builder.Default
    private Set<Category> children = new HashSet<>();

    public void addChild(final Category category) {
        this.children.add(category);
    }

    public Optional<Category> getParent() {
        return Optional.ofNullable(parent);
    }

}

