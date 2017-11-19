package net.lipecki.watson.receipt;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddReceiptItemAmount {

    private String count;
    private String unit;

}
