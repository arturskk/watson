package net.lipecki.watson.account;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModifyAccountDto {

    private String uuid;
    private String name;

}
