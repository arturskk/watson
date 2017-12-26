package net.lipecki.watson.product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModifyProductData {

    private String uuid;
    private String name;
    private String categoryUuid;

}
