package net.lipecki.watson.report;

import lombok.Builder;
import lombok.Data;
import net.lipecki.watson.expanse.ExpanseCost;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class CategoryReportItem {

    private String uuid;
    private String name;
    private String type;
    private ExpanseCost categoryCost;
    @Builder.Default
    @SuppressWarnings("UnusedAssignment")
    private List<CategoryReportItem> subCategories = new ArrayList<>();

    @SuppressWarnings({"unused", "WeakerAccess"})
    public ExpanseCost getTotalCost() {
        return categoryCost.add(subCategories.stream().map(CategoryReportItem::getTotalCost).reduce(ExpanseCost.ZERO, ExpanseCost::add));
    }

    public void addParent(final CategoryReportItem parent) {
        parent.subCategories.add(this);
    }

}
