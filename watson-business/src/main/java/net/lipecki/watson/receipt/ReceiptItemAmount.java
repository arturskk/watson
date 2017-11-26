package net.lipecki.watson.receipt;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReceiptItemAmount {

    private String count;
    private AmountUnit unit;

}
