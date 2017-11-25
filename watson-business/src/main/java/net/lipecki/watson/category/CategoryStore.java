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
                            .build();
                    parent.addChild(category);
                    collection.put(category.getUuid(), category);
                }
        );
        this.combiner.registerHandler(
                ModifyCategoryCommand.MODIFY_CATEGORY_EVENT,
                (collection, event) -> {
                    final ModifyCategory modifyCategory = event.castPayload(ModifyCategory.class);

                    final Category category = collection.get(modifyCategory.getUuid());
                    modifyCategory
                            .getNameOptional()
                            .ifPresent(category::setName);
                    modifyCategory
                            .getParentUuidOptional()
                            .map(collection::get)
                            .ifPresent(category::setParent);
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
                        .name(Category.ROOT_NAME)
                        .parent(null)
                        .build()
        );
        return categories;
    }

}
