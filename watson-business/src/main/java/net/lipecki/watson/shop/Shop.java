package net.lipecki.watson.shop;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Shop {

    public static final String SHOP_STREAM = "_shop";

    private String uuid;
    private String name;

}
