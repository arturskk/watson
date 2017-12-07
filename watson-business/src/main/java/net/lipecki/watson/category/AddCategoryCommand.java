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

    public Event<AddCategory> addCategory(final AddCategory addCategory) {
        return eventStore.storeEvent(Category.CATEGORY_STREAM, addCategory);
    }

}
