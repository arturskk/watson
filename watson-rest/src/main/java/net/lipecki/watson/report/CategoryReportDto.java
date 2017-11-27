package net.lipecki.watson.report;

import lombok.Builder;
import lombok.Data;
import net.lipecki.watson.expanse.ExpanseCost;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class CategoryReportDto {

    private String from;
    private String to;
    private String totalCost;
    private CategorySummary rootCategory;

    @Data
    @Builder
    public static class CategorySummary {

        private String uuid;
        private String name;
        private ExpanseCost categoryCost;
        @Builder.Default
        @SuppressWarnings("UnusedAssignment")
        private List<CategorySummary> subCategories = new ArrayList<>();

        @SuppressWarnings({"unused", "WeakerAccess"})
        public ExpanseCost getTotalCost() {
            return categoryCost.add(subCategories.stream().map(CategorySummary::getTotalCost).reduce(ExpanseCost.ZERO, ExpanseCost::add));
        }

        public void addSubCategory(final CategorySummary categorySummary) {
            this.subCategories.add(categorySummary);
        }

    }

}
