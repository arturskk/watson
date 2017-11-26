package net.lipecki.watson.shop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class GetShopQuery {

    private final ShopStore shopStore;

    public GetShopQuery(final ShopStore shopStore) {
        this.shopStore = shopStore;
    }

    public Optional<Shop> getShop(final String uuid) {
        return this.shopStore.getShop(uuid);
    }

}
