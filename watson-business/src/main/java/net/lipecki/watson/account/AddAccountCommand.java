package net.lipecki.watson.account;

import net.lipecki.watson.event.Event;
import net.lipecki.watson.event.EventStore;
import org.springframework.stereotype.Service;

@Service
public class AddAccountCommand {

    public static final String ADD_ACCOUNT_EVENT = "_account_add";
    private EventStore eventStore;

    public AddAccountCommand(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    public Event<AddAccount> addAccount(final AddAccount addAccount) {
        return this.eventStore.storeEvent(Account.ACCOUNT_STREAM, ADD_ACCOUNT_EVENT, addAccount);
    }

}
