package net.lipecki.watson.producer;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class AddProducerDto {

    @NotNull
    private String name;

}
