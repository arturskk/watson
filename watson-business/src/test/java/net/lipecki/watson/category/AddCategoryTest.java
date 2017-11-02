package net.lipecki.watson.category;

import net.lipecki.watson.store.Event;
import net.lipecki.watson.store.EventStore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AddCategoryTest {

    private static final String CATEGORY_UUID = "category-0000-0000-0000-000000000001";
    private static final String CATEGORY_TYPE = "category-TYPE";
    @Mock
    private EventStore eventStore;
    @InjectMocks
    private AddCategoryService uut;

    @Test
    public void shouldAddCategoryToEventStore() {
        final String expectedCategoryUuid = CATEGORY_UUID;
        final String categoryName = "Sample category";
        final Event<AddCategory> expectedEvent = Event.<AddCategory> builder()
                .type(AddCategoryService.EVENT_TYPE)
                .payload(
                        AddCategory.builder()
                                .type(CATEGORY_TYPE)
                                .name(categoryName)
                                .build()
                )
                .build();

        // given
        when(eventStore.storeEvent(expectedEvent)).thenReturn(expectedCategoryUuid);

        // when
        final String categoryUuid = uut.addCategory(AddCategory.builder().type(CATEGORY_TYPE).name(categoryName).build());

        // then
        assertThat(categoryUuid).isEqualTo(expectedCategoryUuid);
    }

}
