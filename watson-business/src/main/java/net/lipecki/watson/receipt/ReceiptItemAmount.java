package net.lipecki.watson.receipt;

import lombok.Builder;
import lombok.Data;
import net.lipecki.watson.amount.AmountUnit;

@Data
@Builder
public class ReceiptItemAmount {

    private String count;
    private AmountUnit unit;

}
