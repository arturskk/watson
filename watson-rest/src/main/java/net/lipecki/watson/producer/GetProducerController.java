package net.lipecki.watson.producer;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.rest.Api;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(Api.V1)
public class GetProducerController {

    private final GetProducersQuery query;

    public GetProducerController(final GetProducersQuery query) {
        this.query = query;
    }

    @GetMapping("/producer")
    @Transactional
    public List<Producer> getProducers() {
        return this.query.getProducers();
    }

}
