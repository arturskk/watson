package net.lipecki.watson.category;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.combiner.AggregateStreamCombiner;
import net.lipecki.watson.event.EventStore;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GetCategoriesQuery {

    private final AggregateStreamCombiner<Category> combiner;

    public GetCategoriesQuery(final EventStore eventStore) {
        this.combiner = new AggregateStreamCombiner<>(
                eventStore,
                Category.CATEGORY_STREAM
        );
        this.combiner.registerHandler(
                AddCategoryCommand.ADD_CATEGORY_EVENT,
                (collection, event) -> {
                    final AddCategory addCategory = event.castPayload(AddCategory.class);
                    final Category parent = collection.get(addCategory.getParentUuidOptional().orElse(Category.ROOT_UUID));
                    final Category category = Category.builder()
                            .name(addCategory.getName())
                            .type(addCategory.getType())
                            .uuid(event.getStreamId())
                            .parent(parent)
                            .categoryPath(Category.combineCategoryPath(addCategory.getName(), parent))
                            .build();
                    parent.addChild(category);
                    collection.put(category.getUuid(), category);
                }
        );
    }

    public List<Category> getCategories(final String categoryType) {
        final Map<String, Category> categories = this.combiner.get(rootCategoryInitializer(categoryType));
        return categories.values()
                .stream()
                .filter(category -> category.getType().equals(categoryType))
                .collect(Collectors.toList());
    }

    private static Supplier<Map<String, Category>> rootCategoryInitializer(final String categoryType) {
        return () -> {
            final Map<String, Category> categories = new HashMap<>();
            categories.put(
                    Category.ROOT_UUID,
                    Category.builder()
                            .type(categoryType)
                            .uuid(Category.ROOT_UUID)
                            .name(Category.ROOT_NAME_KEY)
                            .categoryPath(Category.ROOT_NAME_KEY)
                            .parent(null)
                            .build()
            );
            return categories;
        };
    }

}
