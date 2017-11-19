package net.lipecki.watson.category;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.combiner.AggregateStreamCombiner;
import net.lipecki.watson.event.EventStore;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class CategoryStore {

    private final AggregateStreamCombiner<Category> combiner;

    public CategoryStore(final EventStore eventStore) {
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

    public Map<String, Category> getCategories() {
        return this.combiner.get(CategoryStore::rootCategoryInitializer);
    }

    private static Map<String, Category> rootCategoryInitializer() {
        final Map<String, Category> categories = new HashMap<>();
        categories.put(
                Category.ROOT_UUID,
                Category.builder()
                        .type(Category.ROOT_TYPE)
                        .uuid(Category.ROOT_UUID)
                        .name(Category.ROOT_NAME_KEY)
                        .categoryPath(Category.ROOT_NAME_KEY)
                        .parent(null)
                        .build()
        );
        return categories;
    }

}
