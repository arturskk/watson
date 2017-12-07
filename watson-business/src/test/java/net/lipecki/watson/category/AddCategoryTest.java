package net.lipecki.watson.category;

import net.lipecki.watson.event.Event;
import net.lipecki.watson.event.EventStore;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class AddCategoryTest {

    private static final String CATEGORY_UUID = "category-0000-0000-0000-000000000001";
    private static final String CATEGORY_TYPE = "category-EVENT_TYPE";
    private EventStore eventStore;
    private AddCategoryCommand uut;

    @Before
    public void setUp() {
        this.eventStore = mock(EventStore.class);
        this.uut = new AddCategoryCommand(eventStore);
    }

    @Test
    public void shouldAddCategoryToEventStore() {
        final String expectedCategoryUuid = CATEGORY_UUID;
        final String categoryName = "Sample category";

        // given
        final AddCategory expectedAddCategory = AddCategory.builder().type(CATEGORY_TYPE).name(categoryName).build();
        when(
                eventStore.storeEvent(Category.CATEGORY_STREAM, expectedAddCategory)
        ).thenReturn(
                Event.<AddCategory> builder().streamId(expectedCategoryUuid).build()
        );

        // when
        final Event<AddCategory> storedEvent = uut.addCategory(expectedAddCategory);

        // then
        assertThat(storedEvent.getStreamId()).isEqualTo(expectedCategoryUuid);
    }

}
