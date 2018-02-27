package net.lipecki.watson.receipt.item.uuidgenerator;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.event.EventStore;
import net.lipecki.watson.projection.ProjectionHandler;
import net.lipecki.watson.receipt.Receipt;
import net.lipecki.watson.receipt.ReceiptAdded;
import net.lipecki.watson.receipt.ReceiptItemAdded;
import net.lipecki.watson.receipt.ReceiptItemModified;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class GenerateItemUuidForReceiptAdded implements ProjectionHandler<ReceiptAdded> {

    private EventStore eventStore;
    private ReceiptItemUuidGeneratorRepository repository;

    public GenerateItemUuidForReceiptAdded(final EventStore eventStore, final ReceiptItemUuidGeneratorRepository repository) {
        this.eventStore = eventStore;
        this.repository = repository;
    }

    @Override
    public Class<ReceiptAdded> getPayloadClass() {
        return ReceiptAdded.class;
    }

    @Override
    public boolean canHandle(final Event event) {
        return getPayloadClass().equals(event.getPayload().getClass()) && event.getPayload().getSchemaVersion() < ReceiptAdded.SchemaVersion.V2;
    }

    @Override
    public void accept(final Event event, final ReceiptAdded payload) {
        if (repository.countByReceiptUuid(event.getStreamId()) > 0) {
            log.debug("Receipt already converted, skipping [receiptUuid={}]", event.getStreamId());
        } else {
            log.debug("Generate uuid for receipt added event with schema less than 2");
            final List<ReceiptItemUuidGeneratorEntity> changedItems = new ArrayList<>();
            final List<ReceiptItemAdded> items = payload.getItems();
            for (int itemIndex = 0; itemIndex < items.size(); ++itemIndex) {
                changedItems.add(
                        ReceiptItemUuidGeneratorEntity
                                .builder()
                                .receiptUuid(event.getStreamId())
                                .itemOldUuid(ReceiptAdded.combineItemUuid(event.getStreamId(), itemIndex))
                                .itemNewUuid(UUID.randomUUID().toString())
                                .build()
                );
            }
            changedItems.stream()
                    .map(
                            item -> ReceiptItemModified
                                    .builder()
                                    .uuid(item.getItemOldUuid())
                                    .newUuid(item.getItemNewUuid())
                                    .build()
                    )
                    .forEach(
                            receiptItemModified -> eventStore.storeEvent(Receipt.RECEIPT_STREAM, receiptItemModified)
                    );
            repository.saveAll(changedItems);
        }
    }

}
