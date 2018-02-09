package net.lipecki.watson.category;

import net.lipecki.watson.combiner.TestAggregateCombinerWithCacheFactory;
import net.lipecki.watson.event.EventStore;
import net.lipecki.watson.event.InMemoryEventStore;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GetCategoriesTest {

    private static final String CATEGORY_TYPE = "category-type";
    private EventStore eventStore;
    private GetCategoriesQuery uut;

    @Before
    public void setUp() {
        this.eventStore = new InMemoryEventStore();
        this.uut = new GetCategoriesQuery(new CategoryStore(TestAggregateCombinerWithCacheFactory.of(eventStore), new CategoryAddedEventHandler(), new CategoryModifiedEventHandler()));
    }

    @Test
    public void shouldReturnNonNullCategories() {
        // when
        final List<Category> categories = uut.getCategories(CATEGORY_TYPE);

        // then
        assertThat(categories).isNotNull();
    }

    @Test
    public void shouldGetStoredCategories() {
        final String expectedCategoryName = "expected-category-name";

        // given
        eventStore.storeEvent(
                Category.CATEGORY_STREAM,
                CategoryAdded.builder().type(CATEGORY_TYPE).name(expectedCategoryName).build()
        );

        // when
        final List<Category> categories = uut.getCategories(CATEGORY_TYPE);

        // then
        assertThat(categories)
                .extracting(Category::getName)
                .contains(expectedCategoryName);
    }

    @Test
    public void shouldReturnCategoryPath() {
        final String expectedCategoryName = "expected-category-name";

        // given
        eventStore.storeEvent(
                Category.CATEGORY_STREAM,
                CategoryAdded.builder().type(CATEGORY_TYPE).name(expectedCategoryName).build()
        );

        // when
        final List<Category> categories = uut.getCategories(CATEGORY_TYPE);

        // then
        assertThat(categories)
                .extracting(Category::getCategoryPath)
                .contains(Arrays.asList(Category.ROOT_NAME, expectedCategoryName));
    }

}
