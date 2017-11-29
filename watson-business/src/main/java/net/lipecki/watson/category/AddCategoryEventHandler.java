package net.lipecki.watson.category;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.combiner.AggregateCombinerHandler;
import net.lipecki.watson.event.Event;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class AddCategoryEventHandler implements AggregateCombinerHandler<Category> {

    @Override
    public void accept(final Map<String, Category> collection, final Event<?> event) {
        final AddCategory addCategory = event.castPayload(AddCategory.class);
        final Category parent = collection.get(addCategory.getParentUuidOptional().orElse(Category.ROOT_UUID));
        final Category category = Category.builder()
                .name(addCategory.getName())
                .type(addCategory.getType())
                .uuid(event.getStreamId())
                .build();
        Category.linkCategoryWithParent(category, parent);
        collection.put(category.getUuid(), category);
    }

}
