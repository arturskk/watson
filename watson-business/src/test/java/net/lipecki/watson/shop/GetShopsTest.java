package net.lipecki.watson.shop;

import net.lipecki.watson.combiner.TestAggregateCombinerWithCacheFactory;
import net.lipecki.watson.event.EventStore;
import net.lipecki.watson.event.InMemoryEventStore;
import net.lipecki.watson.reatialchain.GetRetailChainQuery;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class GetShopsTest {

    private EventStore eventStore;
    private GetShopsQuery uut;
    private GetRetailChainQuery retailChainQueryMock;

    @Before
    public void setUp() {
        eventStore = new InMemoryEventStore();
        retailChainQueryMock = mock(GetRetailChainQuery.class);
        uut = new GetShopsQuery(
                new ShopStore(
                        TestAggregateCombinerWithCacheFactory.of(eventStore),
                        new ShopAddedEventHandler(retailChainQueryMock),
                        new ShopModifiedEventHandler(retailChainQueryMock)
                )
        );
    }

    @Test
    public void shouldGetAllShops() {
        final String expectedShopName = "expectedShopName";

        // given
        eventStore.storeEvent(
                Shop.SHOP_STREAM,
                ShopAdded
                        .builder()
                        .name(expectedShopName)
                        .build()
        );

        // when
        final List<Shop> shops = uut.getShops();

        // then
        assertThat(shops)
                .isNotNull()
                .isNotEmpty();
        assertThat(shops)
                .extracting(Shop::getName)
                .contains(expectedShopName);
    }

}
