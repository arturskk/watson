package net.lipecki.watson.category;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.combiner.AggregateCombinerHandler;
import net.lipecki.watson.event.Event;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class CategoryModifiedEventHandler implements AggregateCombinerHandler<Category, CategoryModified> {

    @Override
    public Class<CategoryModified> getPayloadClass() {
        return CategoryModified.class;
    }

    @Override
    public void accept(final Map<String, Category> collection, final Event event, final CategoryModified payload) {
        final Category category = collection.get(payload.getUuid());
        payload
                .getNameOptional()
                .ifPresent(category::setName);
        payload
                .getParentUuidOptional()
                .map(collection::get)
                .ifPresent(Category.linkToParent(category));
    }

}
