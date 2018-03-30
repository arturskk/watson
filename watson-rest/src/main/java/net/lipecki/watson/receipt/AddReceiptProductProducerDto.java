package net.lipecki.watson.receipt;

import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
@Builder
public class AddReceiptProductProducerDto {

    private String uuid;
    private String name;

    public Optional<String> getUuid() {
        return Optional.ofNullable(this.uuid);
    }

    public Optional<String> getName() {
        return Optional.ofNullable(this.name);
    }

}
