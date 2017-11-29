package net.lipecki.watson.category;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.combiner.AggregateCombinerHandler;
import net.lipecki.watson.event.Event;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class ModifyCategoryEventHandler implements AggregateCombinerHandler<Category> {

    @Override
    public void accept(Map<String, Category> collection, Event<?> event) {
        final ModifyCategory modifyCategory = event.castPayload(ModifyCategory.class);
        final Category category = collection.get(modifyCategory.getUuid());
        modifyCategory
                .getNameOptional()
                .ifPresent(category::setName);
        modifyCategory
                .getParentUuidOptional()
                .map(collection::get)
                .ifPresent(Category.linkToParent(category));
    }

}
