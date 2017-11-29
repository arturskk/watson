package net.lipecki.watson.receipt;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AddReceiptItem {

    private AddReceiptItemProduct product;
    private List<String> tags;
    private String cost;
    private AddReceiptItemAmount amount;

}
