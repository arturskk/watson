package net.lipecki.watson.account;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddAccountData {

    private String name;
    private Boolean useDefault;

}
