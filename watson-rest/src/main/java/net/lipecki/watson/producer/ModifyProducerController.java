package net.lipecki.watson.producer;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.rest.Api;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(Api.V1)
public class ModifyProducerController {

    private final ModifyProducerCommand command;

    public ModifyProducerController(final ModifyProducerCommand command) {
        this.command = command;
    }

    @PutMapping("/producer/{uuid}")
    @Transactional
    public Event modifyProducer(
            @PathVariable final String uuid,
            @Validated @RequestBody final ModifyProducerDto dto) {
        log.info("Request to modify producer [uuid={}, dto={}]", uuid, dto);
        return command.modifyProducer(
                ModifyProducerData
                        .builder()
                        .uuid(uuid)
                        .name(dto.getName())
                        .build()
        );
    }

}
