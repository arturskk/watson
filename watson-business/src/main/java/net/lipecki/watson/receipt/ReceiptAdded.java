package net.lipecki.watson.receipt;

import lombok.Builder;
import lombok.Data;
import net.lipecki.watson.event.EventPayload;

import java.util.List;

@Data
@Builder
public class ReceiptAdded implements EventPayload {

    private String description;
    private String date;
    private String accountUuid;
    private String shopUuid;
    private String categoryUuid;
    private List<String> tags;
    private List<ReceiptItemAdded> items;
    private Long schemaVersion;

    public static final class SchemaVersion {
        /** First event version */
        public static final long V1 = 1;
        /** Receipt items with mandatory uuid */
        public static final long V2 = 2;
    }

    /**
     * For receipts before schema 2.
     * Use receipt uuid with item index as item uuid.
     * @param receiptUuid
     * @param index
     * @since schema version 2
     * @return
     */
    public static String combineItemUuid(final String receiptUuid, final long index) {
        return receiptUuid + "#" + index;
    }

    public long getSchemaVersion() {
        return schemaVersion != null ? schemaVersion : SchemaVersion.V1;
    }

}
