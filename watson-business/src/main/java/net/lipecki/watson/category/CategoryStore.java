package net.lipecki.watson.category;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.combiner.AggregateCombiner;
import net.lipecki.watson.combiner.AggregateCombinerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
class CategoryStore {

    private final AggregateCombiner<Category> combiner;

    public CategoryStore(
            final AggregateCombinerFactory aggregateCombinerFactory,
            final CategoryAddedEventHandler categoryAddedEventHandler,
            final CategoryModifiedEventHandler categoryModifiedEventHandler) {
        this.combiner = aggregateCombinerFactory.getAggregateCombiner(Collections.singletonList(Category.CATEGORY_STREAM), CategoryStore::rootCategoryInitializer);
        this.combiner.addHandler(categoryAddedEventHandler);
        this.combiner.addHandler(categoryModifiedEventHandler);
    }

    public List<Category> getCategories() {
        return this.combiner.getAsList();
    }

    public Optional<Category> getCategory(final String uuid) {
        return Optional.ofNullable(
                this.combiner.get().get(uuid)
        );
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
