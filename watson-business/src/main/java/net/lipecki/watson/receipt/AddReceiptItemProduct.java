package net.lipecki.watson.receipt;

import lombok.Builder;
import lombok.Data;

@SuppressWarnings("WeakerAccess")
@Data
@Builder
public class AddReceiptItemProduct {

    private String uuid;
    private String categoryUuid;
    private String producerUuid;

}
