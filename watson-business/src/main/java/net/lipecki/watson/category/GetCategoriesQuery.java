package net.lipecki.watson.category;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.WatsonException;
import net.lipecki.watson.store.Event;
import net.lipecki.watson.store.EventStore;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
@Service
public class GetCategoriesQuery {

    private EventStore eventStore;

    public GetCategoriesQuery(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    public List<Category> getCategories(final String categoryType) {
        final Map<String, Category> categories = new HashMap<>();

        this.eventStore
                .getEventsByStream(Category.CATEGORY_STREAM)
                .stream()
                .map(this::eventAsCommand)
                .forEach(command -> command.accept(categories));

        return new ArrayList<>(categories.values());
    }

    private Consumer<Map<String, Category>> eventAsCommand(final Event<?> event) {
        if (event.getType().equals(AddCategoryCommand.ADD_CATEGORY_EVENT)) {
            final AddCategory addCategory = event.castPayload(AddCategory.class);
            return categories -> categories.put(
                    event.getAggregateId(),
                    Category.builder()
                            .name(addCategory.getName())
                            .type(addCategory.getType())
                            .uuid(event.getAggregateId())
                            .parent(null)
                            .categoryPath("")
                            .build()
            );
        } else {
            throw WatsonException.of("Unsupported category event type").put("eventType", event.getType());
        }
    }

}
