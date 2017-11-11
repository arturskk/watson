package net.lipecki.watson.receipt;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddReceiptItemAmout {

    private String count;
    private String unit;

}
