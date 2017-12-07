package net.lipecki.watson.product;

import net.lipecki.watson.event.Event;
import net.lipecki.watson.event.EventStore;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class AddProductTest {

    private static final String PRODUCT_NAME = "any-product-name";

    private EventStore eventStore;
    private AddProductCommand uut;

    @Before
    public void setUp() {
        this.eventStore = mock(EventStore.class);
        this.uut = new AddProductCommand(eventStore);
    }

    @Test
    public void shouldAddProductToEventStore() {
        final String expectedProductUuid = UUID.randomUUID().toString();

        // given
        final AddProduct expectedAddProduct = AddProduct.builder().name(PRODUCT_NAME).build();
        when(
                eventStore.storeEvent(Product.PRODUCT_STREAM, expectedAddProduct)
        ).thenReturn(
                Event.<AddProduct> builder().streamId(expectedProductUuid).build()
        );

        // when
        final Event<AddProduct> result = uut.addProduct(
                expectedAddProduct
        );

        // then
        assertThat(result.getStreamId()).isEqualTo(expectedProductUuid);
    }

}
