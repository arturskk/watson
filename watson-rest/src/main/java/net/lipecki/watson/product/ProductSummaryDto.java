package net.lipecki.watson.product;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductSummaryDto {

    private String uuid;
    private String name;
    private String defaultUnit;
    private ProductSummaryCategory category;

    @Data
    @Builder
    static class ProductSummaryCategory {

        private String uuid;
        private String name;
        private List<String> path;

    }

}
