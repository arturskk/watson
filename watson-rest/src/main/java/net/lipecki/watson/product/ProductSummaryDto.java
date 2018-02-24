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
    private Category category;
    private Producer producer;

    @Data
    @Builder
    static class Category {

        private String uuid;
        private String name;
        private List<String> path;

    }

    @Data
    @Builder
    static class Producer {

        private String uuid;
        private String name;

    }

}
