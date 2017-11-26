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

    public CategoryStore(
            final EventStore eventStore,
            final AddCategoryEventHandler addCategoryEventHandler,
            final ModifyCategoryEventHandler modifyCategoryEventHandler) {
        this.combiner = new AggregateStreamCombiner<>(eventStore, Category.CATEGORY_STREAM);
        this.combiner.registerHandler(AddCategoryCommand.ADD_CATEGORY_EVENT, addCategoryEventHandler);
        this.combiner.registerHandler(ModifyCategoryCommand.MODIFY_CATEGORY_EVENT, modifyCategoryEventHandler);
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
