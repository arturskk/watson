package net.lipecki.watson.shop;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.lipecki.watson.reatialchain.RetailChain;

import java.util.Optional;

@EqualsAndHashCode(of = "uuid")
@Data
@Builder
public class Shop {

    public static final String SHOP_STREAM = "_shop";

    private String uuid;
    private String name;
    private RetailChain retailChain;

    public Optional<RetailChain> getRetailChainOptional() {
        return Optional.ofNullable(retailChain);
    }

}
