package net.lipecki.watson.account;

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
public class AddAccountTest {

    private static final String ACCOUNT_NAME = "any-account-name";
    @Mock
    private EventStore eventStore;
    @InjectMocks
    private AddAccountCommand uut;

    @Test
    public void shouldAddAccountToEventStore() {
        final String expectedAccountUuid = UUID.randomUUID().toString();

        // given
        final AddAccount expectedAddAccount = AddAccount.builder().name(ACCOUNT_NAME).build();
        when(
                eventStore.storeEvent(Account.ACCOUNT_STREAM, AddAccountCommand.ADD_ACCOUNT_EVENT, expectedAddAccount)
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
