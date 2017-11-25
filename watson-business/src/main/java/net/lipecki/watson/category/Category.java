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

    public static final String CATEGORY_STREAM = "_category";
    public static final String ROOT_UUID = "root";
    public static final String ROOT_NAME = "Root";
    public static final String ROOT_TYPE = "root";
    public static final String PATH_SEPARATOR = " > ";

    private String uuid;
    private String type;
    private String name;
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

    public boolean isRootCategory() {
        return ROOT_TYPE.equals(this.type);
    }

    public boolean isTypeOf(final String categoryType) {
        return categoryType.equals(this.type);
    }

    public String getCategoryPath() {
        if (parent == null) {
            return name;
        } else {
            return parent.getCategoryPath() + PATH_SEPARATOR + name;
        }
    }

}

