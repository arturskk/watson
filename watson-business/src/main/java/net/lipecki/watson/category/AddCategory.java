package net.lipecki.watson.category;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddCategory {

    private String type;
    private String name;

}
