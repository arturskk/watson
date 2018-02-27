package net.lipecki.watson.receipt;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.lipecki.watson.category.Category;
import net.lipecki.watson.cost.Cost;
import net.lipecki.watson.product.Product;

import java.util.List;

@EqualsAndHashCode(of = "uuid")
@Data
@Builder
public class ReceiptItem {

    public static final String CATEGORY_TYPE = "_category_receipt_item";

    private String uuid;
    private Cost cost;
    private List<String> tags;
    private Product product;
    private ReceiptItemAmount amount;

    public Category getCategory() {
        return product.getCategory();
    }

}
