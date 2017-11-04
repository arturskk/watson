package net.lipecki.watson.category;

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
public class GetCategoriesTest {

    public static final String CATEGORY_TYPE = "category-type";
    @Spy
    private EventStore eventStore = new InMemoryEventStore();
    @InjectMocks
    private GetCategoriesQuery uut;

    @Test
    public void shouldGetEmptyWhenNoCategories() {
        // when
        final List<Category> categories = uut.getCategories(CATEGORY_TYPE);

        // then
        assertThat(categories).isNotNull().isEmpty();
    }

    @Test
    public void shouldGetStoredCategories() {
        final String expectedCategoryName = "expected-category-name";

        // given
        eventStore.storeEvent(
                Category.CATEGORY_STREAM,
                AddCategoryCommand.ADD_CATEGORY_EVENT,
                AddCategory.builder().type(CATEGORY_TYPE).name(expectedCategoryName).build()
        );

        // when
        final List<Category> categories = uut.getCategories(CATEGORY_TYPE);

        // then
        assertThat(categories).hasSize(1);
        final Category category = categories.get(0);
        assertThat(category.getName()).isEqualTo(expectedCategoryName);
    }

}
