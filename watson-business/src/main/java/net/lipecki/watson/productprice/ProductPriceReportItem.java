package net.lipecki.watson.productprice;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductPriceReportItem {

    private Long id;
    private List<CategoryDto> categoryPath;
    private ProductDto product;
    private ShopDto shop;
    private ReceiptInfoDto receipt;
    private String date;
    private String pricePerUnit;
    private String unit;

    @Data
    @Builder
    static class ShopDto {
        private String uuid;
        private String name;
        private String retailChainName;
    }

    @Data
    @Builder
    static class ProductDto {
        private String uuid;
        private String name;
        private String producerName;
    }

    @Data
    @Builder
    static class CategoryDto {
        private String uuid;
        private String name;
    }

    @Data
    @Builder
    static class ReceiptInfoDto {
        private String uuid;
        private String itemUuid;
    }

}
