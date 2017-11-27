package net.lipecki.watson.expanse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExpanseDto {

    private String name;
    private String date;
    private String cost;
    private ExpanseCategoryDto category;

    @Data
    @Builder
    public static class ExpanseCategoryDto {

        private String uuid;
        private String name;
        private String parentUuid;

    }

}
