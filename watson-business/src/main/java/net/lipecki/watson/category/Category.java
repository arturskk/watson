package net.lipecki.watson.category;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

@EqualsAndHashCode(of = "uuid")
@ToString(of = {"uuid", "name", "type", "children"})
@Data
@Builder
public class Category {

    public static final String CATEGORY_STREAM = "_category";
    public static final String ROOT_UUID = "root";
    public static final String ROOT_NAME = "Root";
    public static final String ROOT_TYPE = "root";

    private String uuid;
    private String type;
    private String name;
    private Category parent;
    @SuppressWarnings("UnusedAssignment")
    @Builder.Default
    private Set<Category> children = new HashSet<>();

    public static Consumer<Category> linkToParent(final Category category) {
        return parent -> linkCategoryWithParent(category, parent);
    }

    public static Consumer<Category> linkToChildren(final Category parent) {
        return category -> linkCategoryWithParent(category, parent);
    }

    public static void linkCategoryWithParent(final Category category, final Category parent) {
        Objects.requireNonNull(category);
        if (category.parent != parent) {
            // remove from current parent children if has parent
            if (category.parent != null) {
                category.parent.children.remove(category);
            }
            // change category parent
            category.parent = parent;
            // add to parents children if has parent
            if (parent != null) {
                parent.children.add(category);
            }
        }
    }

    public void addChild(final Category category) {
        linkCategoryWithParent(category, this);
    }

    public void setParent(final Category parent) {
        linkCategoryWithParent(this, parent);
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

    public List<String> getCategoryPath() {
        final List<String> path = new LinkedList<>();
        if (parent != null) {
            path.addAll(parent.getCategoryPath());
        }
        path.add(name);
        return path;
    }

    public void accept(final Consumer<Category> visitor) {
        visitor.accept(this);
        children.forEach(child -> child.accept(visitor));
    }

    public int getDepth() {
        return parent != null ? parent.getDepth() + 1 : 0;
    }

}

