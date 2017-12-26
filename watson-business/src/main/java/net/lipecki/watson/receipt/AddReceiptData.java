package net.lipecki.watson.receipt;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AddReceiptData {

    private String description;
    private String date;
    private String accountUuid;
    private String shopUuid;
    private String categoryUuid;
    private List<String> tags;
    private List<AddReceiptItemData> items;

}
