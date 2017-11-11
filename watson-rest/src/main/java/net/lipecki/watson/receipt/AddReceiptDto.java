package net.lipecki.watson.receipt;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class AddReceiptDto {

    private String description;
    @NotNull private String date;
    @NotNull private AddReceiptAccountDto account;
    @NotNull private AddReceiptShopDto shop;
    @NotNull private AddReceiptCategoryDto category;
    @NotNull @NotEmpty private List<AddReceiptItemDto> items;
    private List<String> tags;

    public List<String> getTags() {
        return tags != null ? tags : new ArrayList<>();
    }

    public List<AddReceiptItemDto> getItems() {
        return items != null ? items : new ArrayList<>();
    }

}
