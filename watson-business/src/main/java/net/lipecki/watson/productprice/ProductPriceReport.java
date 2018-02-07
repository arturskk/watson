package net.lipecki.watson.productprice;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ProductPriceReport {

    private String categoryUuid;
    private boolean includeSubcategories;
    @Singular
    private List<ProductPriceReportItem> items;

}
