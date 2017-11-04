package net.lipecki.watson.product;

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
public class AddProductTest {

    private static final String PRODUCT_NAME = "any-product-name";
    @Mock
    private EventStore eventStore;
    @InjectMocks
    private AddProductCommand uut;

    @Test
    public void shouldAddProductToEventStore() {
        final String expectedProductUuid = UUID.randomUUID().toString();

        // given
        final AddProduct expectedAddProduct = AddProduct.builder().name(PRODUCT_NAME).build();
        when(
                eventStore.storeEvent(Product.PRODUCT_STREAM, AddProductCommand.ADD_PRODUCT_EVENT, expectedAddProduct)
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
