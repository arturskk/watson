package net.lipecki.watson.category;

import net.lipecki.watson.event.Event;
import net.lipecki.watson.event.EventStore;
import org.springframework.stereotype.Service;

@Service
public class AddCategoryCommand {

    private final EventStore eventStore;

    public AddCategoryCommand(final EventStore eventStore) {
        this.eventStore = eventStore;
    }

    public Event addCategory(final AddCategoryData data) {
        return eventStore.storeEvent(Category.CATEGORY_STREAM, asEventPayload(data));
    }

    private CategoryAdded asEventPayload(final AddCategoryData data) {
        return CategoryAdded
                .builder()
                .name(data.getName())
                .type(data.getType())
                .parentUuid(data.getParentUuid())
                .build();
    }

}
