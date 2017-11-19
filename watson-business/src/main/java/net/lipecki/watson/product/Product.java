package net.lipecki.watson.product;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.lipecki.watson.category.Category;

@EqualsAndHashCode(of = "uuid")
@Data
@Builder
public class Product {

    public static final String PRODUCT_STREAM = "_product";

    private String uuid;
    private String name;
    private Category category;

}
