package net.lipecki.watson.productprice;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ProductPriceReport {

    private String categoryUuid;
    private boolean includeSubcategories;
    @SuppressWarnings("UnusedAssignment")
    @Builder.Default
    private List<ProductPriceReportItem> items = new ArrayList<>();

}
