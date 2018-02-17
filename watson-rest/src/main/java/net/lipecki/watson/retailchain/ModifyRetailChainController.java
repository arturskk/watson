package net.lipecki.watson.retailchain;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.reatialchain.ModifyRetailChainCommand;
import net.lipecki.watson.reatialchain.ModifyRetailChainData;
import net.lipecki.watson.rest.Api;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(Api.V1)
public class ModifyRetailChainController {

    private final ModifyRetailChainCommand command;

    public ModifyRetailChainController(final ModifyRetailChainCommand command) {
        this.command = command;
    }

    @PutMapping("/retailchain/{uuid}")
    @Transactional
    public Event modifyProduct(
            @PathVariable final String uuid,
            @Validated @RequestBody final ModifyRetailChainDto dto) {
        log.info("Request to modify retail chain [uuid={}, dto={}]", uuid, dto);
        return command.modifyRetailChain(
                ModifyRetailChainData
                        .builder()
                        .uuid(uuid)
                        .name(dto.getName())
                        .build()
        );
    }

}
