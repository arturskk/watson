package net.lipecki.watson.category;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.combiner.AggregateStreamCombiner;
import net.lipecki.watson.store.EventStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GetCategoriesQuery {

    private final AggregateStreamCombiner<Category> combiner;

    public GetCategoriesQuery(final EventStore eventStore) {
        this.combiner = new AggregateStreamCombiner<>(eventStore);
        this.combiner.registerHandler(
                AddCategoryCommand.ADD_CATEGORY_EVENT,
                (collection, event) -> collection.put(
                        event.getAggregateId(),
                        Category.builder()
                                .name(event.castPayload(AddCategory.class).getName())
                                .type(event.castPayload(AddCategory.class).getType())
                                .uuid(event.getAggregateId())
                                .parent(null)
                                .categoryPath("")
                                .build()
                )
        );
    }

    public List<Category> getCategories(final String categoryType) {
        final Map<String, Category> categories = getAllCategories();
        return categories.values()
                .stream()
                .filter(category -> category.getType().equals(categoryType))
                .collect(Collectors.toList());
    }

    private Map<String, Category> getAllCategories() {
        return this.combiner.get(Category.CATEGORY_STREAM);
    }

}
