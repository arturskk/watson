package net.lipecki.watson.expanse;

import lombok.Builder;
import lombok.Data;
import net.lipecki.watson.category.Category;
import net.lipecki.watson.cost.Cost;

import java.time.LocalDate;

@Data
@Builder
public class Expanse {

    private String name;
    private LocalDate date;
    private String type;
    private String refUuid;
    private Category category;
    private Cost cost;

    public String getCategoryUuid() {
        return this.category.getUuid();
    }

}
