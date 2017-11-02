package net.lipecki.watson.category;

import net.lipecki.watson.store.Event;
import net.lipecki.watson.store.EventStore;
import org.springframework.stereotype.Service;

@Service
public class AddCategoryService {

    public static final String EVENT_TYPE = "_category_add";

    private final EventStore eventStore;

    public AddCategoryService(final EventStore eventStore) {
        this.eventStore = eventStore;
    }

    public Event<AddCategory> addCategory(final AddCategory addCategory) {
        return eventStore.storeEvent(EVENT_TYPE, addCategory);
    }

}
