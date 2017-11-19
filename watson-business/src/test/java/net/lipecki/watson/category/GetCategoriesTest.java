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
    public void shouldAlwaysReturnRootCategory() {
        // when
        final List<Category> categories = uut.getCategories(CATEGORY_TYPE);

        // then
        assertThat(categories).extracting("uuid").containsExactly(Category.ROOT_UUID);
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
        assertThat(categories).extracting("name").contains(expectedCategoryName);
    }

    @Test
    public void shouldReturnCategoryPath() {
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
        assertThat(categories).extracting("categoryPath").contains(
                String.format("%s%s%s", Category.ROOT_NAME_KEY, Category.PATH_SEPARATOR, expectedCategoryName)
        );
    }

}
