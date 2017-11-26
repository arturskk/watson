package net.lipecki.watson.receipt;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.lipecki.watson.category.Category;
import net.lipecki.watson.expanse.ExpanseCost;
import net.lipecki.watson.expanse.ExpanseItem;
import net.lipecki.watson.product.Product;

import java.util.List;

@EqualsAndHashCode(of = "uuid")
@Data
@Builder
public class ReceiptItem implements ExpanseItem {

    public static final String CATEGORY_TYPE = "_category_receipt_item";

    private ExpanseCost cost;
    private List<String> tags;
    private Product product;
    private ReceiptItemAmount amount;

    public String getDescription() {
        return toString();
    }

    @Override
    public Category getCategory() {
        return product.getCategory();
    }

}
