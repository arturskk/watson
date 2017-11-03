package net.lipecki.watson.receipt;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;

@Data
@Builder
public class AddReceiptItemDto {

    private String cost;
    private AddReceiptCategoryDto category;
    private AddReceiptProductDto product;
    @Singular
    private List<String> tags;

}
