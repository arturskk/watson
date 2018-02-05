package net.lipecki.watson.report.categoryexpanse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryExpanseReport {

    private String from;
    private String to;
    private String totalCost;
    private CategoryExpanseReportItem rootCategory;

}
