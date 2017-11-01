package net.lipecki.watson.receipt;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class AddReceipt {

    private LocalDate date;
    private String budgetUuid;
    private String accountUuid;
    private String shopUuid;
    private String categoryUuid;
    private List<String> tags;
    private List<AddReceiptItem> items;
    
}
