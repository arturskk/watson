package net.lipecki.watson.account;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class AddAccountDto {

    @NotNull
    private String name;

}
