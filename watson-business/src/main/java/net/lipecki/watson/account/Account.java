package net.lipecki.watson.account;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(of = "uuid")
@Data
@Builder
public class Account {

    public static final String ACCOUNT_STREAM = "_account";

    private String uuid;
    private String name;
    private Boolean useDefault;

}
