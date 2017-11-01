package net.lipecki.watson.receipt;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AddReceiptDto {

    private String date;
    private String budgetUuid;
    private AddReceiptAccountDto account;
    private AddReceiptShopDto shop;
    private AddReceiptCategoryDto category;
    private List<String> tags;
    private List<AddReceiptItemDto> items;

}
