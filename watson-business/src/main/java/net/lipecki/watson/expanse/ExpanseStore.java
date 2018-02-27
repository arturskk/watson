package net.lipecki.watson.expanse;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.combiner.AggregateCombiner;
import net.lipecki.watson.combiner.AggregateCombinerFactory;
import net.lipecki.watson.receipt.Receipt;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
class ExpanseStore {

    private final AggregateCombiner<Expanse> combiner;

    public ExpanseStore(final AggregateCombinerFactory aggregateCombinerFactory, final ExpanseReceiptAddedEventHandler expanseReceiptAddedEventHandler) {
        combiner = aggregateCombinerFactory.getAggregateCombiner(Collections.singletonList(Receipt.RECEIPT_STREAM));
        combiner.setIgnoreUnhandledEvents(true);
        combiner.addHandler(expanseReceiptAddedEventHandler);
    }

    public List<Expanse> getExpanses(final LocalDate from, final LocalDate to) {
        return this.combiner
                .getAsList()
                .stream()
                .filter(expanse -> from == null || !expanse.getDate().isBefore(from))
                .filter(expanse -> to == null || !expanse.getDate().isAfter(to))
                .collect(Collectors.toList());
    }

}

