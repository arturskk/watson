package net.lipecki.watson.receipt;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReceiptSummaryDto {

    private String uuid;
    private String cost;
    private String date;
    private String categoryName;
    private List<String> categoryPath;
    private String accountName;
    private String shopName;
    private List<ReceiptSummaryItem> items;

    @Data
    @Builder
    public static class ReceiptSummaryItem {

        private String productName;
        private String categoryName;
        private List<String> categoryPath;
        private String amount;
        private String unit;
        private String cost;

    }

}
