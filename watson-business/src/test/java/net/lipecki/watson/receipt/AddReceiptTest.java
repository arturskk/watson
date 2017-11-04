package net.lipecki.watson.receipt;

import net.lipecki.watson.event.Event;
import net.lipecki.watson.event.EventStore;
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
    private AddReceiptCommand uut;

    @Test
    public void shouldStoreAddReceiptEvent() {
        final AddReceipt expectedAddReceipt = AddReceipt.builder().build();
        final String expectedUuid = UUID.randomUUID().toString();

        // given
        when(
                eventStore.storeEvent(Receipt.RECEIPT_STREAM, AddReceiptCommand.ADD_RECEIPT_EVENT, expectedAddReceipt)
        ).thenReturn(
                Event.<AddReceipt> builder().streamId(expectedUuid).build()
        );

        // when
        final Event<AddReceipt> storedEvent = uut.addReceipt(expectedAddReceipt);

        // then
        assertThat(storedEvent.getStreamId()).isEqualTo(expectedUuid);
    }

}
