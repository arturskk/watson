package net.lipecki.watson.category;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.event.EventStore;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ModifyCategoryCommand {

    private final EventStore eventStore;

    public ModifyCategoryCommand(final EventStore eventStore) {
        this.eventStore = eventStore;
    }

    public Event modifyCategory(final ModifyCategoryData data) {
        return this.eventStore.storeEvent(Category.CATEGORY_STREAM, asEventPayload(data));
    }

    private CategoryModified asEventPayload(final ModifyCategoryData data) {
        return CategoryModified
                .builder()
                .uuid(data.getUuid())
                .parentUuid(data.getParentUuid())
                .name(data.getName())
                .build();
    }

}
