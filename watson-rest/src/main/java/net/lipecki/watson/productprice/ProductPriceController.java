package net.lipecki.watson.productprice;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.projection.ProjectionStatus;
import net.lipecki.watson.rest.Api;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(Api.V1)
public class ProductPriceController {

    private final ProductPriceProjectionService projectionService;

    public ProductPriceController(final ProductPriceProjectionService projectionService) {
        this.projectionService = projectionService;
    }

    @GetMapping("/product-price/projection/status")
    public ProjectionStatus getProjectionStatus() {
        return this.projectionService.getProjectionStatus();
    }

    @PostMapping("/product-price/projection/reset")
    @Transactional
    public ProjectionStatus resetProjection() {
        return this.projectionService.resetProjection();
    }

}
