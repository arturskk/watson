package net.lipecki.watson.shop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GetShopsQuery {

    private final ShopStore shopStore;

    public GetShopsQuery(final ShopStore shopStore) {
        this.shopStore = shopStore;
    }

    public List<Shop> getShops() {
        return this.shopStore.getShops();
    }

}
