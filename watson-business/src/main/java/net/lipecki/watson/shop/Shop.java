package net.lipecki.watson.shop;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(of = "uuid")
@Data
@Builder
public class Shop {

    public static final String SHOP_STREAM = "_shop";

    private String uuid;
    private String name;

}
