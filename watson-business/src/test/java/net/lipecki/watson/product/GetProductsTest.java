package net.lipecki.watson.product;

import net.lipecki.watson.category.GetCategoryQuery;
import net.lipecki.watson.event.EventStore;
import net.lipecki.watson.event.InMemoryEventStore;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;


public class GetProductsTest {

    private EventStore eventStore;
    private GetProductsQuery uut;
    private AddProductEventHandler addProductEventHandler;
    private GetCategoryQuery categoryQuery;
    private ModifyProductEventHandler modifyProductEventHandler;

    @Before
    public void setUp() {
        categoryQuery = mock(GetCategoryQuery.class);
        addProductEventHandler = new AddProductEventHandler(categoryQuery);
        modifyProductEventHandler = new ModifyProductEventHandler(categoryQuery);
        eventStore = new InMemoryEventStore();
        uut = new GetProductsQuery(new ProductStore(eventStore, addProductEventHandler, modifyProductEventHandler));
    }

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
