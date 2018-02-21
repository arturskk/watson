package net.lipecki.watson.productprice;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.WatsonException;
import net.lipecki.watson.category.Category;
import net.lipecki.watson.category.GetCategoryQuery;
import net.lipecki.watson.product.GetProductQuery;
import net.lipecki.watson.product.Product;
import net.lipecki.watson.reatialchain.RetailChain;
import net.lipecki.watson.shop.GetShopQuery;
import net.lipecki.watson.shop.Shop;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GetProductPriceQuery {

    private final ProductPriceRepository repository;
    private final GetCategoryQuery categoryQuery;
    private final GetShopQuery shopQuery;
    private final GetProductQuery productQuery;

    public GetProductPriceQuery(
            final ProductPriceRepository repository,
            final GetCategoryQuery categoryQuery,
            final GetShopQuery shopQuery,
            final GetProductQuery productQuery) {
        this.repository = repository;
        this.categoryQuery = categoryQuery;
        this.shopQuery = shopQuery;
        this.productQuery = productQuery;
    }

    public ProductPriceReport getProductPriceReport(final String categoryUuid, final boolean includeSubcategories) {
        final Category category = categoryQuery
                .getCategory(categoryUuid)
                .orElseThrow(WatsonException.supplier("Unknown category uuid: " + categoryUuid));

        final ProductPriceReport.ProductPriceReportBuilder report = ProductPriceReport
                .builder()
                .categoryUuid(categoryUuid)
                .includeSubcategories(includeSubcategories);

        final List<ProductPriceReportItem> items = new ArrayList<>();
        if (includeSubcategories) {
            category.accept(subcategory -> items.addAll(getProductPriceReportItems(subcategory)));
        } else {
            items.addAll(getProductPriceReportItems(category));
        }
        report.items(items);

        final ProductPriceReport result = report.build();
        final Comparator<ProductPriceReportItem> byName = Comparator.comparing(item1 -> item1.getProduct().getName());
        final Comparator<ProductPriceReportItem> byDateDesc = Comparator.comparing(ProductPriceReportItem::getDate).reversed();
        result.getItems().sort(byName.thenComparing(byDateDesc));

        return result;
    }

    private List<ProductPriceReportItem> getProductPriceReportItems(final Category category) {
        return repository
                .findAllByCategoryUuid(category.getUuid())
                .map(price -> asReportItem(price, category))
                .collect(Collectors.toList());
    }

    private ProductPriceReportItem asReportItem(final ProductPriceEntity price, final Category category) {
        final Shop shop = shopQuery
                .getShop(price.getShopUuid())
                .orElseThrow(WatsonException.supplier("Missing shop for uuid: " + price.getShopUuid()));
        final Product product = productQuery
                .getProduct(price.getProductUuid())
                .orElseThrow(WatsonException.supplier("Missing product for uuid: " + price.getProductUuid()));

        return ProductPriceReportItem
                .builder()
                .id(price.getId())
                .categoryPath(asCategoryPath(category))
                .product(asProductDto(product))
                .shop(asShopDto(shop))
                .receipt(asReceiptDto())
                .date(price.getDate())
                .pricePerUnit(price.getPricePerUnit())
                .unit(price.getUnit())
                .build();
    }

    private List<ProductPriceReportItem.CategoryDto> asCategoryPath(final Category category) {
        final List<ProductPriceReportItem.CategoryDto> path = new ArrayList<>();

        Optional<Category> pathPart = Optional.of(category);
        while (pathPart.isPresent()) {
            path.add(
                    0,
                    pathPart.map(this::asCategory)
                            .orElseThrow(WatsonException.supplier("Path part should not be null"))
            );
            pathPart = pathPart.flatMap(Category::getParent);
        }

        return path;
    }

    private ProductPriceReportItem.CategoryDto asCategory(final Category part) {
        return ProductPriceReportItem
                .CategoryDto
                .builder()
                .name(part.getName())
                .uuid(part.getUuid())
                .build();
    }

    private ProductPriceReportItem.ProductDto asProductDto(final Product product) {
        return ProductPriceReportItem.ProductDto.builder().name(product.getName()).uuid(product.getUuid()).build();
    }

    private ProductPriceReportItem.ShopDto asShopDto(final Shop shop) {
        return ProductPriceReportItem.ShopDto
                .builder()
                .name(shop.getName())
                .uuid(shop.getUuid())
                .retailChainName(shop.getRetailChainOptional().map(RetailChain::getName).orElse(null))
                .build();
    }

    private ProductPriceReportItem.ReceiptInfoDto asReceiptDto() {
        return ProductPriceReportItem.ReceiptInfoDto.builder().build();
    }

}
