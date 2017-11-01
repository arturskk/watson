package net.lipecki.watson.category;

import com.google.common.collect.ImmutableMap;
import net.lipecki.watson.store.AddEvent;
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
        final AddEvent expectedEvent = AddEvent.builder()
                .type(Category.STREAM_TYPE)
                .payload(
                        ImmutableMap.of(
                                "name", categoryName,
                                "type", CATEGORY_TYPE
                        )
                )
                .build();

        // given
        when(eventStore.storeEvent(expectedEvent)).thenReturn(expectedCategoryUuid);

        // when
        final String categoryUuid = uut.addCategory(CATEGORY_TYPE, categoryName);

        // then
        assertThat(categoryUuid).isEqualTo(expectedCategoryUuid);
    }

}
