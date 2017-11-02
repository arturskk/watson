package net.lipecki.watson.receipt;

import lombok.Data;
import net.lipecki.watson.account.Account;
import net.lipecki.watson.expanse.Expanse;
import net.lipecki.watson.category.Category;
import net.lipecki.watson.expanse.ExpanseCost;

import java.time.LocalDate;
import java.util.List;

@Data
public class Receipt implements Expanse {

    public static final String RECEIPT_STREAM = "_receipt";
    public static final String CATEGORY_TYPE = "_category_receipt";

    private LocalDate date;
    private String uuid;
    private Account account;
    private List<ReceiptItem> items;
    private Category category;

    // shop instance

    public ExpanseCost getCost() {
        return this.items
                .stream()
                .map(ReceiptItem::getCost)
                .reduce((a, b) -> a.add(b))
                .orElse(ExpanseCost.empty());
    }

}
