package net.lipecki.watson.shop;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.combiner.AggregateCombiner;
import net.lipecki.watson.combiner.AggregateCombinerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
class ShopStore {

    private final AggregateCombiner<Shop> combiner;

    public ShopStore(final AggregateCombinerFactory aggregateCombinerFactory) {
        this.combiner = aggregateCombinerFactory.getAggregateCombiner(Collections.singletonList(Shop.SHOP_STREAM));
        this.combiner.addHandler(
                AddShopCommand.ADD_SHOP_EVENT,
                (collection, event) -> collection.put(
                        event.getStreamId(),
                        Shop.builder()
                                .uuid(event.getStreamId())
                                .name(event.castPayload(AddShop.class).getName())
                                .build()
                )
        );
    }

    public List<Shop> getShops() {
        return this.combiner.getAsList();
    }

    public Optional<Shop> getShop(final String uuid) {
        return Optional.ofNullable(
                this.combiner.get().get(uuid)
        );
    }

}
