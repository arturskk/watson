package net.lipecki.watson.shop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GetShopsQuery {

    private final ShopStore shopStore;

    public GetShopsQuery(final ShopStore shopStore) {
        this.shopStore = shopStore;
    }

    public List<Shop> getShops() {
        return this.shopStore
                .getShops()
                .stream()
                .sorted(Comparator.comparing(Shop::getName, Collator.getInstance(Locale.forLanguageTag("pl"))))
                .collect(Collectors.toList());
    }

}
