package net.lipecki.watson.category;

import net.lipecki.watson.store.Event;
import net.lipecki.watson.store.EventStore;
import org.springframework.stereotype.Service;

@Service
public class AddCategoryCommand {

    public static final String ADD_CATEGORY_EVENT = "_category_add";

    private final EventStore eventStore;

    public AddCategoryCommand(final EventStore eventStore) {
        this.eventStore = eventStore;
    }

    public Event<AddCategory> addCategory(final AddCategory addCategory) {
        return eventStore.storeEvent(Category.CATEGORY_STREAM, ADD_CATEGORY_EVENT, addCategory);
    }

}
