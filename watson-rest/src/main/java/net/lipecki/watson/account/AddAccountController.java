package net.lipecki.watson.account;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.rest.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(Api.V1)
public class AddAccountController {

    private final AddAccountCommand addAccountCommand;

    public AddAccountController(final AddAccountCommand addAccountCommand) {
        this.addAccountCommand = addAccountCommand;
    }

    @PostMapping("/account")
    public Event<AddAccount> addAccount(@Validated @RequestBody AddAccountDto dto) {
        log.info("Request to add account [dto={}]", dto);
        return addAccountCommand.addAccount(
                AddAccount.builder()
                        .name(dto.getName())
                        .build()
        );
    }

}
