package net.lipecki.watson.account;

import net.lipecki.watson.event.Event;
import net.lipecki.watson.event.EventStore;
import org.springframework.stereotype.Service;

@Service
public class AddAccountCommand {

    private EventStore eventStore;

    public AddAccountCommand(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    public Event<AddAccount> addAccount(final AddAccount addAccount) {
        return this.eventStore.storeEvent(Account.ACCOUNT_STREAM, addAccount);
    }

}
