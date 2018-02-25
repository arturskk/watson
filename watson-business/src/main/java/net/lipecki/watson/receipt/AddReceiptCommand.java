package net.lipecki.watson.receipt;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.event.EventStore;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AddReceiptCommand {

    private final EventStore eventStore;

    public AddReceiptCommand(final EventStore eventStore) {
        this.eventStore = eventStore;
    }

    public Event addReceipt(final AddReceiptData data) {
        return eventStore.storeEvent(Receipt.RECEIPT_STREAM, asEventPayload(data));
    }

    private ReceiptAdded asEventPayload(final AddReceiptData data) {
        return ReceiptAdded
                .builder()
                .accountUuid(data.getAccountUuid())
                .shopUuid(data.getShopUuid())
                .categoryUuid(data.getCategoryUuid())
                .date(data.getDate())
                .description(data.getDescription())
                .tags(data.getTags())
                .items(data.getItems().stream().map(this::asReceiptItemAdded).collect(Collectors.toList()))
                .build();
    }

    private ReceiptItemAdded asReceiptItemAdded(final AddReceiptItemData data) {
        // TODO: move Cost.of from handler?
        return ReceiptItemAdded
                .builder()
                .uuid(UUID.randomUUID().toString())
                .productUuid(data.getProductUuid())
                .amount(data.getAmount())
                .cost(data.getCost())
                .tags(data.getTags())
                .build();
    }

}
