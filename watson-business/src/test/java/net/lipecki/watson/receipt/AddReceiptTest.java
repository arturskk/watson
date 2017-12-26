package net.lipecki.watson.receipt;

import net.lipecki.watson.event.Event;
import net.lipecki.watson.event.EventStore;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class AddReceiptTest {

    private EventStore eventStore;
    private AddReceiptCommand uut;

    @Before
    public void setUp() {
        this.eventStore = mock(EventStore.class);
        this.uut = new AddReceiptCommand(eventStore);
    }

    @Test
    public void shouldStoreAddReceiptEvent() {
        final ReceiptAdded expectedAddReceipt = ReceiptAdded.builder().items(new ArrayList<>()).build();
        final String expectedUuid = UUID.randomUUID().toString();

        // given
        when(
                eventStore.storeEvent(Receipt.RECEIPT_STREAM, expectedAddReceipt)
        ).thenReturn(
                Event.<ReceiptAdded> builder().streamId(expectedUuid).build()
        );

        // when
        final Event storedEvent = uut.addReceipt(AddReceiptData.builder().items(new ArrayList<>()).build());

        // then
        assertThat(storedEvent.getStreamId()).isEqualTo(expectedUuid);
    }

}
