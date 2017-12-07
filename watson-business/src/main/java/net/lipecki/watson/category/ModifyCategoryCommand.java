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

    public Event<ModifyCategory> modifyCategory(final ModifyCategory modifyCategory) {
        return this.eventStore.storeEvent(
                Category.CATEGORY_STREAM,
                modifyCategory
        );
    }

}
