package net.lipecki.watson.producer;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.rest.Api;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(Api.V1)
public class AddProducerController {

    private final AddProducerCommand command;

    public AddProducerController(final AddProducerCommand command) {
        this.command = command;
    }

    @PostMapping("/producer")
    @Transactional
    public Event addProducer(@Validated @RequestBody AddProducerDto dto) {
        log.info("Request to add producer [dto={}]", dto);
        return command.addProducer(
                AddProducerData
                        .builder()
                        .name(dto.getName())
                        .build()
        );
    }

}
