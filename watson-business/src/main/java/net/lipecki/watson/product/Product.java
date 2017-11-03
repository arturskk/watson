package net.lipecki.watson.product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Product {

    public static final String PRODUCT_STREAM = "_product";

    private String uuid;
    private String name;

}
