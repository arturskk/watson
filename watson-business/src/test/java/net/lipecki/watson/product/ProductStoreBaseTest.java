package net.lipecki.watson.product;

import net.lipecki.watson.category.GetCategoryQuery;
import net.lipecki.watson.combiner.TestAggregateCombinerWithCacheFactory;
import net.lipecki.watson.event.InMemoryEventStore;
import net.lipecki.watson.producer.GetProducerQuery;
import org.junit.Before;

import static org.mockito.Mockito.mock;

public abstract class ProductStoreBaseTest {

    protected ProductStore productStore;
    protected GetCategoryQuery categoryQuery;
    protected InMemoryEventStore eventStore;
    protected GetProducerQuery producerQuery;

    @Before
    public void setUpProductStore() {
        categoryQuery = mock(GetCategoryQuery.class);
        producerQuery = mock(GetProducerQuery.class);
        eventStore = new InMemoryEventStore();
        productStore = new ProductStore(
                TestAggregateCombinerWithCacheFactory.of(eventStore),
                new ProductAddedEventHandler(categoryQuery, producerQuery),
                new ProductModifiedEventHandler(categoryQuery, producerQuery)
        );
    }

}
