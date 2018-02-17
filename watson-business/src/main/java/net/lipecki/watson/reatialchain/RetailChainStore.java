package net.lipecki.watson.reatialchain;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.combiner.AggregateCombiner;
import net.lipecki.watson.combiner.AggregateCombinerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class RetailChainStore {

    private final AggregateCombiner<RetailChain> combiner;

    public RetailChainStore(
            final AggregateCombinerFactory aggregateCombinerFactory,
            final RetailChainAddedEventHandler addedHandler,
            final RetailChainModifiedEventHandler modifiedHandler) {
        this.combiner = aggregateCombinerFactory.getAggregateCombiner(Collections.singletonList(RetailChain.RETAIL_CHAIN_STREAM));
        this.combiner.addHandler(addedHandler);
        this.combiner.addHandler(modifiedHandler);
    }

    public List<RetailChain> getRetailChains() {
        return combiner.getAsList();
    }

}
