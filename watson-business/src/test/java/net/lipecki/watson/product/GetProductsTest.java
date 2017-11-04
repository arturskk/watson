package net.lipecki.watson.product;

import net.lipecki.watson.event.EventStore;
import net.lipecki.watson.event.InMemoryEventStore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class GetProductsTest {

    @Spy
    private EventStore eventStore = new InMemoryEventStore();
    @InjectMocks
    private GetProductsQuery uut;

    @Test
    public void shouldGetAllProducts() {
        final String expectedProductName = "expectedProductName";

        // given
        eventStore.storeEvent(
                Product.PRODUCT_STREAM,
                AddProductCommand.ADD_PRODUCT_EVENT,
                AddProduct.builder()
                        .name(expectedProductName)
                        .build()
        );

        // when
        final List<Product> products = uut.getProducts();

        // then
        assertThat(products).isNotNull().isNotEmpty();
        assertThat(products).extracting("name").contains(expectedProductName);
    }

}
