package net.lipecki.watson.account;

import net.lipecki.watson.event.Event;
import net.lipecki.watson.event.EventStore;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AddAccountTest {

    private static final String ACCOUNT_NAME = "any-account-name";
    private EventStore eventStore;
    private AddAccountCommand uut;

    @Before
    public void setUp() {
        this.eventStore = mock(EventStore.class);
        this.uut = new AddAccountCommand(eventStore);
    }

    @Test
    public void shouldAddAccountToEventStore() {
        final String expectedAccountUuid = UUID.randomUUID().toString();

        // given
        final AddAccount expectedAddAccount = AddAccount.builder().name(ACCOUNT_NAME).build();
        when(
                eventStore.storeEvent(Account.ACCOUNT_STREAM, expectedAddAccount)
        ).thenReturn(
                Event.<AddAccount> builder().streamId(expectedAccountUuid).build()
        );

        // when
        final Event<AddAccount> result = uut.addAccount(
                expectedAddAccount
        );

        // then
        assertThat(result.getStreamId()).isEqualTo(expectedAccountUuid);
    }

}
