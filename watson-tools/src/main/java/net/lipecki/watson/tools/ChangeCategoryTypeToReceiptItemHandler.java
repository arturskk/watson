package net.lipecki.watson.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.receipt.ReceiptItem;

import java.sql.ResultSet;
import java.util.Map;

@Slf4j
public class ChangeCategoryTypeToReceiptItemHandler implements EventsTableRowHandler {

    public static final String COLUMN_PAYLOAD_CLASS = "payload_class";
    public static final String COLUMN_PAYLOAD = "payload";
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean isApplicable(final ResultSet eventRow) throws Exception {
        return eventRow.getString(COLUMN_PAYLOAD_CLASS).equals("net.lipecki.watson.category.CategoryAdded");
    }

    @Override
    public void handle(final ResultSet eventRow, final boolean dryRun) throws Exception {
        final Map payloadMap = objectMapper.readValue(
                eventRow.getString(COLUMN_PAYLOAD),
                Map.class
        );
        payloadMap.put("type", ReceiptItem.CATEGORY_TYPE);
        log.info("Event after modification [payloadMap={}]", payloadMap);
        if (!dryRun) {
            eventRow.updateString(COLUMN_PAYLOAD, objectMapper.writeValueAsString(payloadMap));
        }
    }

}
