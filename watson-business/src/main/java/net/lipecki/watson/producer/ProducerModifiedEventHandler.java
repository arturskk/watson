package net.lipecki.watson.producer;

import net.lipecki.watson.combiner.AggregateCombinerHandler;
import net.lipecki.watson.event.Event;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ProducerModifiedEventHandler implements AggregateCombinerHandler<Producer, ProducerModified> {

    @Override
    public Class<ProducerModified> getPayloadClass() {
        return ProducerModified.class;
    }

    @Override
    public void accept(final Map<String, Producer> collection, final Event event, final ProducerModified payload) {
        final Producer model = collection.get(payload.getUuid());
        payload
                .getNameOptional()
                .ifPresent(model::setName);
    }

}
