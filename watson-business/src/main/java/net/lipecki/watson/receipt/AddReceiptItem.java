package net.lipecki.watson.receipt;

import lombok.Builder;
import lombok.Data;
import net.lipecki.watson.WatsonException;

import java.util.List;

@Data
@Builder
public class AddReceiptItem {

    private AddReceiptItemProduct product;
    private String productUuid;
    private List<String> tags;
    private String cost;
    private AddReceiptItemAmount amount;

    public String getProductUuid() {
        if (this.productUuid != null) {
            return this.productUuid;
        } else if (product != null) {
            return this.product.getUuid();
        } else {
            throw WatsonException.of("AddReceiptItem without any product uuid!");
        }
    }

}
