package net.lipecki.watson.receipt;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;

@Data
@Builder
public class AddReceipt {

    private String description;
    private String date;
    private String accountUuid;
    private String shopUuid;
    private String categoryUuid;
    @Singular
    private List<String> tags;
    @Singular
    private List<AddReceiptItem> items;
    
}
