package net.lipecki.watson.shop;

import net.lipecki.watson.event.Event;
import net.lipecki.watson.event.EventStore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AddShopTest {

    private static final String SHOP_NAME = "any-shop-name";
    @Mock
    private EventStore eventStore;
    @InjectMocks
    private AddShopCommand uut;

    @Test
    public void shouldAddShopToEventStore() {
        final String expectedShopUuid = UUID.randomUUID().toString();

        // given
        final AddShop expectedAddShop = AddShop.builder().name(SHOP_NAME).build();
        when(
                eventStore.storeEvent(Shop.SHOP_STREAM, AddShopCommand.ADD_SHOP_EVENT, expectedAddShop)
        ).thenReturn(
                Event.<AddShop> builder().streamId(expectedShopUuid).build()
        );

        // when
        final Event<AddShop> result = uut.addShop(
                expectedAddShop
        );

        // then
        assertThat(result.getStreamId()).isEqualTo(expectedShopUuid);
    }

}
