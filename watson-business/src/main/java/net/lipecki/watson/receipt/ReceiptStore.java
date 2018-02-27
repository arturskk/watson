package net.lipecki.watson.receipt;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.combiner.AggregateCombiner;
import net.lipecki.watson.combiner.AggregateCombinerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
class ReceiptStore {

    private final AggregateCombiner<Receipt> combiner;

    public ReceiptStore(
            final AggregateCombinerFactory aggregateCombinerFactory,
            final ReceiptAddedEventHandler receiptAddedEventHandler,
            final ReceiptItemModifiedUuidEventHandler receiptItemModifiedUuidEventHandler) {
        this.combiner = aggregateCombinerFactory.getAggregateCombiner(getClass().getSimpleName(), Collections.singletonList(Receipt.RECEIPT_STREAM));
        combiner.addHandler(receiptAddedEventHandler);
        combiner.addHandler(receiptItemModifiedUuidEventHandler);
    }

    public List<Receipt> getReceipts() {
        return this.combiner.getAsList();
    }

}
