package net.lipecki.watson.receipt;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.store.AddEvent;
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

    public String addReceipt(final AddReceipt addReceipt) {
        return eventStore.storeEvent(
                AddEvent.builder()
                        .type(EVENT_TYPE)
                        .payload(addReceipt)
                        .build()
        );
    }

}
