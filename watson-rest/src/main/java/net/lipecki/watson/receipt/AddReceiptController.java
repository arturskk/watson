package net.lipecki.watson.receipt;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.store.Event;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
public class AddReceiptController {

    private final AddReceiptWithDependenciesCommand addReceiptWithDependenciesCommand;

    public AddReceiptController(final AddReceiptWithDependenciesCommand addReceiptWithDependenciesCommand) {
        this.addReceiptWithDependenciesCommand = addReceiptWithDependenciesCommand;
    }

    @PostMapping("/receipt")
    public Event<AddReceipt> addReceipt(@Valid @RequestBody final AddReceiptDto addReceipt) {
        log.info("Request to add new receipt [addReceipt={}]", addReceipt);

        return this.addReceiptWithDependenciesCommand.addReceipt(addReceipt);
    }

}
