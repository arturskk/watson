package net.lipecki.watson.producer;

import net.lipecki.watson.combiner.AggregateCombinerHandler;
import net.lipecki.watson.event.Event;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ProducerAddedEventHandler implements AggregateCombinerHandler<Producer, ProducerAdded> {

    @Override
    public Class<ProducerAdded> getPayloadClass() {
        return ProducerAdded.class;
    }

    @Override
    public void accept(final Map<String, Producer> collection, final Event event, final ProducerAdded payload) {
        collection.put(
                event.getStreamId(),
                Producer.builder()
                        .uuid(event.getStreamId())
                        .name(payload.getName())
                        .build()
        );
    }

}
