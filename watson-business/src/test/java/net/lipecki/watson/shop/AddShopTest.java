package net.lipecki.watson.shop;

import net.lipecki.watson.event.Event;
import net.lipecki.watson.event.EventStore;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AddShopTest {

    private static final String SHOP_NAME = "any-shop-name";

    private EventStore eventStore;
    private AddShopCommand uut;

    @Before
    public void setUp() {
        this.eventStore = mock(EventStore.class);
        this.uut = new AddShopCommand(eventStore);
    }

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
