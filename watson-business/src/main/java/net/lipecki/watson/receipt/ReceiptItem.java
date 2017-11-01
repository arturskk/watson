package net.lipecki.watson.receipt;

import lombok.Builder;
import lombok.Data;
import net.lipecki.watson.category.Category;
import net.lipecki.watson.expanse.ExpanseCost;
import net.lipecki.watson.expanse.ExpanseItem;
import net.lipecki.watson.product.Product;

import java.util.List;

@Data
@Builder
public class ReceiptItem implements ExpanseItem {

    private ExpanseCost cost;
    private Category category;
    private List<String> tags;
    private Product product;

    public String getDescription() {
        return toString();
    }

}
