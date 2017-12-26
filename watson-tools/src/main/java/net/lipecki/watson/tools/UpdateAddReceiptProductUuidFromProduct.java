package net.lipecki.watson.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

@Slf4j
public class UpdateAddReceiptProductUuidFromProduct implements EventsTableRowHandler {

    public static final String COLUMN_PAYLOAD_CLASS = "payload_class";
    public static final String COLUMN_PAYLOAD = "payload";
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean isApplicable(final ResultSet eventRow) throws Exception {
        return eventRow.getString(COLUMN_PAYLOAD_CLASS).equals("net.lipecki.watson.receipt.ReceiptAdded");
    }

    @Override
    public void handle(final ResultSet eventRow, final boolean dryRun) throws Exception {
        final Map payloadMap = objectMapper.readValue(
                eventRow.getString(COLUMN_PAYLOAD),
                Map.class
        );
        for (final Map item : (List<Map>) payloadMap.get("items")) {
            final Map product = (Map) item.get("product");
            if (product != null) {
                log.info("Product to migrate for ReceiptItemAdded [product={}]", product);
                item.put("productUuid", product.get("uuid"));
            }
        }
        if (!dryRun) {
            eventRow.updateString(COLUMN_PAYLOAD, objectMapper.writeValueAsString(payloadMap));
        }
    }

}
