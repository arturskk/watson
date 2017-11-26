package net.lipecki.watson.category;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.event.Event;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.BiConsumer;

@Slf4j
@Service
public class AddCategoryEventHandler implements BiConsumer<Map<String, Category>, Event<?>> {

    @Override
    public void accept(final Map<String, Category> collection, final Event<?> event) {
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

}
