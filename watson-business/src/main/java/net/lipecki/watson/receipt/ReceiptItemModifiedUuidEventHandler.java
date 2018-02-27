package net.lipecki.watson.receipt;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.WatsonException;
import net.lipecki.watson.combiner.AggregateCombinerHandler;
import net.lipecki.watson.event.Event;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class ReceiptItemModifiedUuidEventHandler implements AggregateCombinerHandler<Receipt, ReceiptItemModified> {

    @Override
    public Class<ReceiptItemModified> getPayloadClass() {
        return ReceiptItemModified.class;
    }

    @Override
    public boolean canHandle(final Event event) {
        return getPayloadClass().equals(event.getPayload().getClass()) && event.castPayload(ReceiptItemModified.class).getNewUuidOptional().isPresent();
    }

    @Override
    public void accept(final Map<String, Receipt> collection, final Event event, final ReceiptItemModified payload) {
        final String oldUuid = payload.getUuid();
        if (oldUuid.matches(".*\\#.*")) {
            final String[] uuidParts = oldUuid.split("#");
            final Receipt receipt = collection.get(uuidParts[0]);
            if (receipt == null) {
                log.warn("null receipt");
            }
            receipt.getItems().get(Integer.parseInt(uuidParts[1])).setUuid(payload.getNewUuid());
        } else {
            collection
                    .values()
                    .stream()
                    .flatMap(receipt -> receipt.getItems().stream())
                    .filter(item -> item.getUuid().equals(payload.getUuid()))
                    .findFirst()
                    .orElseThrow(() -> WatsonException.of("Can't find expected receipt item").with("receiptItemUuid", payload.getUuid()))
                    .setUuid(payload.getNewUuid());
        }
    }

}
