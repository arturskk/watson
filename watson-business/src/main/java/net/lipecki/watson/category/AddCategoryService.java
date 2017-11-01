package net.lipecki.watson.category;

import com.google.common.collect.ImmutableMap;
import net.lipecki.watson.store.AddEvent;
import net.lipecki.watson.store.EventStore;
import org.springframework.stereotype.Service;

@Service
public class AddCategoryService {

    private final EventStore eventStore;

    public AddCategoryService(final EventStore eventStore) {
        this.eventStore = eventStore;
    }

    public String addCategory(final String type, final String name) {
        return eventStore.storeEvent(
                AddEvent.builder()
                        .type(Category.STREAM_TYPE)
                        .payload(
                                ImmutableMap.of(
                                        "name", name,
                                        "type", type
                                )
                        )
                        .build()
        );
    }

}
