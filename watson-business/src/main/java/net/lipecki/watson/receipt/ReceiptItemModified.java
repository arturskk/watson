package net.lipecki.watson.receipt;

import lombok.Builder;
import lombok.Data;
import net.lipecki.watson.event.EventPayload;

import java.util.Optional;

@Data
@Builder
public class ReceiptItemModified implements EventPayload {

    private String uuid;
    private String newUuid;

    public Optional<String> getNewUuidOptional() {
        return Optional.ofNullable(newUuid);
    }

}
