package net.lipecki.watson.receipt;

import net.lipecki.watson.store.Event;
import net.lipecki.watson.store.EventStore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AddReceiptTest {

    @Mock
    private EventStore eventStore;
    @InjectMocks
    private AddReceiptService uut;

    @Test
    public void shouldStoreAddReceiptEvent() {
        final AddReceipt expectedAddReceipt = AddReceipt.builder().build();
        final String expectedUuid = UUID.randomUUID().toString();

        // given
        when(
                eventStore.storeEvent(
                        Event.builder()
                                .type(AddReceiptService.EVENT_TYPE)
                                .payload(expectedAddReceipt)
                                .build()
                )
        ).thenReturn(expectedUuid);

        // when
        final String receiptUuid = uut.addReceipt(expectedAddReceipt);

        // then
        assertThat(receiptUuid).isEqualTo(expectedUuid);
    }

}
