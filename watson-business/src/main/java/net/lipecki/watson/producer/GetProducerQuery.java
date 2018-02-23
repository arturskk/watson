package net.lipecki.watson.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class GetProducerQuery {

    private ProducerStore store;

    public GetProducerQuery(ProducerStore store) {
        this.store = store;
    }

    public Optional<Producer> getProducer(String uuid) {
        return store.getProducer(uuid);
    }

}
