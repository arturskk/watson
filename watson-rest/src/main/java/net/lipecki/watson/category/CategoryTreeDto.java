package net.lipecki.watson.category;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class CategoryTreeDto {

    private int depth;
    private String type;
    private String uuid;
    private String name;
    private List<String> path;
    private List<CategoryTreeDto> children;

    public static CategoryTreeDto from(final Category category, final String categoryType) {
        return from(category, categoryType, 0, new ArrayList<>());
    }

    private static CategoryTreeDto from(final Category category, final String categoryType, int depth, List<String> path) {
        return CategoryTreeDto
                .builder()
                .uuid(category.getUuid())
                .name(category.getName())
                .type(category.getType())
                .path(path)
                .depth(depth)
                .children(
                        category.getChildren()
                                .stream()
                                .filter(child -> child.getType().equals(categoryType))
                                .sorted(Comparator.comparing(Category::getName))
                                .map(child -> from(child, categoryType, depth + 1, expandPath(path, category)))
                                .collect(Collectors.toList())
                )
                .build();
    }

    private static List<String> expandPath(final List<String> path, final Category category) {
        final List<String> expandedPath = new ArrayList<>();
        expandedPath.addAll(path);
        expandedPath.add(category.getName());
        return expandedPath;
    }

}
