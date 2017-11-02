package net.lipecki.watson.shop;

import net.lipecki.watson.store.EventStore;
import net.lipecki.watson.store.InMemoryEventStore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class GetShopsTest {

    @Spy
    private EventStore eventStore = new InMemoryEventStore();
    @InjectMocks
    private GetShopsQuery uut;

    @Test
    public void shouldGetAllAccounts() {
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
