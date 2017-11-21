package net.lipecki.watson.receipt;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class AddReceiptItemDto {

    @NotNull private String cost;
    @NotNull private AddReceiptProductDto product;
    @NotNull private AddReceiptAmountDto amount;
    private List<String> tags;

    public List<String> getTags() {
        return tags != null ? tags : new ArrayList<>();
    }

}
