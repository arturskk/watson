package net.lipecki.watson.retailchain;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.reatialchain.AddRetailChainCommand;
import net.lipecki.watson.reatialchain.AddRetailChainData;
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
public class AddRetailChainController {

    private final AddRetailChainCommand command;

    public AddRetailChainController(final AddRetailChainCommand command) {
        this.command = command;
    }

    @PostMapping("/retailchain")
    @Transactional
    public Event addRetailChain(@Validated @RequestBody AddRetailChainDto dto) {
        log.info("Request to add retail chain [dto={}]", dto);
        return command.addRetailChain(
                AddRetailChainData
                        .builder()
                        .name(dto.getName())
                        .build()
        );
    }

}
