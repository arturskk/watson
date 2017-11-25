package net.lipecki.watson.product;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.rest.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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
    public List<ProductSummaryDto> getAllProducts() {
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
