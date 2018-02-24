package net.lipecki.watson.product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModifyProductDto {

    private String name;
    private String categoryUuid;
    private String defaultUnit;
    private String producerUuid;

}
