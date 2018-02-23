package net.lipecki.watson.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GetProducersQuery {

    private ProducerStore store;

    public GetProducersQuery(ProducerStore store) {
        this.store = store;
    }

    public List<Producer> getProducers() {
        return store.getProducers();
    }

}
