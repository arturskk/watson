package net.lipecki.watson.report.categoryexpanse;

import lombok.Builder;
import lombok.Data;
import net.lipecki.watson.cost.Cost;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class CategoryExpanseReportItem {

    private String uuid;
    private String name;
    private String type;
    private Cost categoryCost;
    @Builder.Default
    @SuppressWarnings("UnusedAssignment")
    private List<CategoryExpanseReportItem> subCategories = new ArrayList<>();

    @SuppressWarnings({"unused", "WeakerAccess"})
    public Cost getTotalCost() {
        return categoryCost.add(subCategories.stream().map(CategoryExpanseReportItem::getTotalCost).reduce(Cost.ZERO, Cost::add));
    }

    public void addParent(final CategoryExpanseReportItem parent) {
        parent.subCategories.add(this);
    }

}
