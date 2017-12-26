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
    
}
