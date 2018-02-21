package net.lipecki.watson.shop;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModifyShop {

    private String uuid;
    private String name;
    private String retailChainUuid;

}
