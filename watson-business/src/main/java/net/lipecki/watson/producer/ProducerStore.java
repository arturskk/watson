package net.lipecki.watson.producer;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.combiner.AggregateCombiner;
import net.lipecki.watson.combiner.AggregateCombinerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ProducerStore {

    private final AggregateCombiner<Producer> combiner;

    public ProducerStore(
            final AggregateCombinerFactory aggregateCombinerFactory,
            final ProducerAddedEventHandler addedHandler,
            final ProducerModifiedEventHandler modifiedHandler) {
        this.combiner = aggregateCombinerFactory.getAggregateCombiner(getClass().getSimpleName(), Collections.singletonList(Producer.PRODUCER_STREAM));
        this.combiner.addHandler(addedHandler);
        this.combiner.addHandler(modifiedHandler);
    }

    public List<Producer> getProducers() {
        return combiner.getAsList();
    }

    public Optional<Producer> getProducer(final String uuid) {
        return Optional.ofNullable(
                this.combiner.get().get(uuid)
        );
    }

}
