package net.lipecki.watson.receipt;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReceiptItemAdded {

    private String uuid;
    private String productUuid;
    private List<String> tags;
    private String cost;
    private AddReceiptItemAmount amount;

    /**
     * For receipts before schema 2.
     * Use receipt uuid with item index as item uuid.
     * @param receiptUuid
     * @param index
     * @since schema version 2
     * @return
     */
    public static String combineUuidBasedOnIndex(final String receiptUuid, final long index) {
        return receiptUuid + "#" + index;
    }

}
