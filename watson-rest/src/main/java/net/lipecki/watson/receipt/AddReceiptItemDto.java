package net.lipecki.watson.receipt;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Optional;

@Data
@Builder
public class AddReceiptItemDto {

    private Optional<String> description;
    private AddReceiptCategoryDto category;
    private AddReceiptProductDto product;
    private List<String> tags;
    private String cost;

}
