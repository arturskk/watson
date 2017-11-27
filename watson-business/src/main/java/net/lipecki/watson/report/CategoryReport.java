package net.lipecki.watson.report;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
class CategoryReport {

    private String from;
    private String to;
    private String totalCost;
    private CategoryReportItem rootCategory;

}
