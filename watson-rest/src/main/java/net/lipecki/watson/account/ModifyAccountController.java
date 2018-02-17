package net.lipecki.watson.account;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.rest.Api;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(Api.V1)
public class ModifyAccountController {

    private final ModifyAccountCommand modifyAccountCommand;

    public ModifyAccountController(final ModifyAccountCommand modifyAccountCommand) {
        this.modifyAccountCommand = modifyAccountCommand;
    }

    @PutMapping("/account/{uuid}")
    @Transactional
    public Event modifyProduct(
            @PathVariable final String uuid,
            @Validated @RequestBody final ModifyAccountDto dto) {
        log.info("Request to modify account [uuid={}, dto={}]", uuid, dto);
        return modifyAccountCommand.modifyAccount(
                ModifyAccount
                        .builder()
                        .uuid(uuid)
                        .name(dto.getName())
                        .useDefault(dto.getUseDefault())
                        .build()
        );
    }

}
