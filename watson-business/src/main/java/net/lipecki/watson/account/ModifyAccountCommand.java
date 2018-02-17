package net.lipecki.watson.account;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.event.EventStore;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ModifyAccountCommand {

    private final EventStore eventStore;

    public ModifyAccountCommand(final EventStore eventStore) {
        this.eventStore = eventStore;
    }

    public Event modifyAccount(final ModifyAccount data) {
        return this.eventStore.storeEvent(Account.ACCOUNT_STREAM, asEventPayload(data));
    }

    private AccountModified asEventPayload(final ModifyAccount data) {
        return AccountModified
                .builder()
                .uuid(data.getUuid())
                .name(data.getName())
                .useDefault(data.getUseDefault())
                .build();
    }

}
