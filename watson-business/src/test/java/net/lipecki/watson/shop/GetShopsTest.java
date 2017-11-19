package net.lipecki.watson.shop;

import net.lipecki.watson.event.EventStore;
import net.lipecki.watson.event.InMemoryEventStore;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GetShopsTest {

    private EventStore eventStore;
    private GetShopsQuery uut;

    @Before
    public void setUp() {
        this.eventStore = new InMemoryEventStore();
        this.uut = new GetShopsQuery(eventStore);
    }

    @Test
    public void shouldGetAllShops() {
        final String expectedShopName = "expectedShopName";

        // given
        eventStore.storeEvent(
                Shop.SHOP_STREAM,
                AddShopCommand.ADD_SHOP_EVENT,
                AddShop.builder()
                        .name(expectedShopName)
                        .build()
        );

        // when
        final List<Shop> shops = uut.getShops();

        // then
        assertThat(shops).isNotNull().isNotEmpty();
        assertThat(shops).extracting("name").contains(expectedShopName);
    }

}
