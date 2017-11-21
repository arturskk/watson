package net.lipecki.watson.receipt;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AddReceiptItem {

    private String productUuid;
    private List<String> tags;
    private String cost;
    private AddReceiptItemAmount amount;

}
