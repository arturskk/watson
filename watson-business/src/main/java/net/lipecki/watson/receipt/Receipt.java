package net.lipecki.watson.receipt;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.lipecki.watson.account.Account;
import net.lipecki.watson.category.Category;
import net.lipecki.watson.expanse.ExpanseCost;
import net.lipecki.watson.shop.Shop;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(of = "uuid")
@Data
@Builder
public class Receipt {

    public static final String RECEIPT_STREAM = "_receipt";
    public static final String CATEGORY_TYPE = "_category_receipt";

    private String uuid;
    private LocalDate date;
    private Account account;
    private List<ReceiptItem> items;
    private Category category;
    private Shop shop;

    // shop instance

    public ExpanseCost getCost() {
        return this.items
                .stream()
                .map(ReceiptItem::getCost)
                .reduce((a, b) -> a.add(b))
                .orElse(ExpanseCost.empty());
    }

}
