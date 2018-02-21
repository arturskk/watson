package net.lipecki.watson.shop;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ListShopDto {

    private String uuid;
    private String name;
    private ListShopDto.RetailChainDto retailChain;

    @Data
    @Builder
    public static class RetailChainDto {

        private String uuid;
        private String name;

    }

}
