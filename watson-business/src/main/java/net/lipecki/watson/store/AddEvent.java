package net.lipecki.watson.store;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class AddEvent {

    private String type;
    // TODO: json vs map?
    private Map<String, Object> payload;

}
