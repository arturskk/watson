package net.lipecki.watson.receipt;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.event.EventStore;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AddReceiptCommand {

    public static final String ADD_RECEIPT_EVENT = "_receipt_add";

    private final EventStore eventStore;

    public AddReceiptCommand(final EventStore eventStore) {
        this.eventStore = eventStore;
    }

    public Event<AddReceipt> addReceipt(final AddReceipt addReceipt) {
        return eventStore.storeEvent(Receipt.RECEIPT_STREAM, ADD_RECEIPT_EVENT, addReceipt);
    }

}
