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
            final ReceiptAddedEventHandler receiptAddedEventHandler) {
        this.combiner = aggregateCombinerFactory.getAggregateCombiner(Collections.singletonList(Receipt.RECEIPT_STREAM));
        this.combiner.addHandler(receiptAddedEventHandler);
    }

    public List<Receipt> getReceipts() {
        return this.combiner.getAsList();
    }

}
