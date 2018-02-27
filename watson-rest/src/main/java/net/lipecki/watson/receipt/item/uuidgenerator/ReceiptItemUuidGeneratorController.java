package net.lipecki.watson.receipt.item.uuidgenerator;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.projection.ProjectionStatus;
import net.lipecki.watson.rest.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(Api.V1)
public class ReceiptItemUuidGeneratorController {

    private final ReceiptItemUuidGeneratorProjectionService projectionService;

    public ReceiptItemUuidGeneratorController(final ReceiptItemUuidGeneratorProjectionService projectionService) {
        this.projectionService = projectionService;
    }

    @GetMapping("/receipt-item-uuid-generator/projection/status")
    public ProjectionStatus getProjectionStatus() {
        return this.projectionService.getProjectionStatus();
    }

}
