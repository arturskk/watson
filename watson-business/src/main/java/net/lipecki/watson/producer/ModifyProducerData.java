package net.lipecki.watson.producer;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModifyProducerData {

    private String uuid;
    private String name;

}
