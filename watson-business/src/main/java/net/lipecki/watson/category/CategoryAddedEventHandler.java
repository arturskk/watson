package net.lipecki.watson.category;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.combiner.AggregateCombinerHandler;
import net.lipecki.watson.event.Event;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class CategoryAddedEventHandler implements AggregateCombinerHandler<Category, CategoryAdded> {

    @Override
    public Class<CategoryAdded> getPayloadClass() {
        return CategoryAdded.class;
    }

    @Override
    public void accept(final Map<String, Category> collection, final Event event, final CategoryAdded payload) {
        final Category parent = collection.get(payload.getParentUuidOptional().orElse(Category.ROOT_UUID));
        final Category category = Category.builder()
                .name(payload.getName())
                .type(payload.getType())
                .uuid(event.getStreamId())
                .build();
        Category.linkCategoryWithParent(category, parent);
        collection.put(category.getUuid(), category);
    }

}
