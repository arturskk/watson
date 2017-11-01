package net.lipecki.watson.receipt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
public class AddReceiptController {

    private final AddReceiptWithDependenciesService addReceiptWithDependenciesService;

    public AddReceiptController(final AddReceiptWithDependenciesService addReceiptWithDependenciesService) {
        this.addReceiptWithDependenciesService = addReceiptWithDependenciesService;
    }

    @PostMapping("/receipt")
    public String addReceipt(@Valid @RequestBody final AddReceiptDto addReceipt) {
        log.info("Request to add new receipt [addReceipt={}]", addReceipt);

        return this.addReceiptWithDependenciesService.addReceipt(addReceipt);
    }

}
