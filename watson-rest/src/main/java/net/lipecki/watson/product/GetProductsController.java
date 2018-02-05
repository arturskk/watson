package net.lipecki.watson.product;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.receipt.AmountUnit;
import net.lipecki.watson.rest.Api;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(Api.V1)
public class GetProductsController {

    private final GetProductsQuery query;

    public GetProductsController(final GetProductsQuery query) {
        this.query = query;
    }

    @GetMapping("/product")
    @Transactional
    public List<ProductSummaryDto> getAllProducts() {
        final Optional<Product> pajacyk = this.query.getProducts().stream().filter(p -> p.getUuid().equals("c88351bb-4a23-4193-99ad-4ad61ab8dc27")).findFirst();
        return this.query
                .getProducts()
                .stream()
                .map(GetProductsController::asProductSummaryDto)
                .collect(Collectors.toList());
    }

    private static ProductSummaryDto asProductSummaryDto(final Product product) {
        return ProductSummaryDto
                .builder()
                .uuid(product.getUuid())
                .name(product.getName())
                .defaultUnit(product.getDefaultUnitOptional().map(AmountUnit::getName).orElse(null))
                .category(
                        ProductSummaryDto
                                .ProductSummaryCategory
                                .builder()
                                .uuid(product.getCategory().getUuid())
                                .name(product.getCategory().getName())
                                .path(product.getCategory().getCategoryPath())
                                .build()
                )
                .build();
    }

}
