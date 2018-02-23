package net.lipecki.watson.producer;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(of = "uuid")
@Data
@Builder
public class Producer {

    public static final String PRODUCER_STREAM = "_producer";

    private String uuid;
    private String name;

}