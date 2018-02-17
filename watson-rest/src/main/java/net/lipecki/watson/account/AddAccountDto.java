package net.lipecki.watson.account;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Data
@Builder
public class AddAccountDto {

    @NotNull
    private String name;
    private Boolean useDefault;

}
