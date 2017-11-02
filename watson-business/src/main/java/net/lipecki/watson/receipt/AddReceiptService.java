package net.lipecki.watson.receipt;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.store.Event;
import net.lipecki.watson.store.EventStore;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AddReceiptService {

    public static final String EVENT_TYPE = "_receipt_add";

    private final EventStore eventStore;

    public AddReceiptService(final EventStore eventStore) {
        this.eventStore = eventStore;
    }

    public Event<AddReceipt> addReceipt(final AddReceipt addReceipt) {
        return eventStore.storeEvent(EVENT_TYPE, addReceipt);
    }

}
