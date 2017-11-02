package net.lipecki.watson.shop;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.combiner.AggregateStreamCombiner;
import net.lipecki.watson.store.EventStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GetShopsQuery {

    private final AggregateStreamCombiner<Shop> combiner;

    public GetShopsQuery(final EventStore eventStore) {
        this.combiner = new AggregateStreamCombiner<>(eventStore);

        this.combiner.registerHandler(
                AddShopCommand.ADD_SHOP_EVENT,
                (collection, event) -> collection.put(
                        event.getAggregateId(),
                        Shop.builder()
                                .uuid(event.getAggregateId())
                                .name(event.castPayload(AddShop.class).getName())
                                .build()
                )
        );
    }

    public List<Shop> getShops() {
        return this.combiner.getAsList(Shop.SHOP_STREAM);
    }

}
