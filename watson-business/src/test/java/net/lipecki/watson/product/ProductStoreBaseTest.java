package net.lipecki.watson.product;

import net.lipecki.watson.category.GetCategoryQuery;
import net.lipecki.watson.event.InMemoryEventStore;
import org.junit.Before;

import static org.mockito.Mockito.mock;

public abstract class ProductStoreBaseTest {

    protected ProductStore productStore;
    protected GetCategoryQuery categoryQuery;
    protected InMemoryEventStore eventStore;

    @Before
    public void setUpProductStore() {
        categoryQuery = mock(GetCategoryQuery.class);
        eventStore = new InMemoryEventStore();
        productStore = new ProductStore(
                eventStore,
                new AddProductEventHandler(categoryQuery),
                new ModifyProductEventHandler(categoryQuery)
        );
    }

}
