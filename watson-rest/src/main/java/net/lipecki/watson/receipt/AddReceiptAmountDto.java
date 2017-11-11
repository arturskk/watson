package net.lipecki.watson.receipt;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class AddReceiptAmountDto {

    @NotNull private String count;
    @NotNull private String unit;

}
