package net.lipecki.watson.account;

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
public class GetAccountsTest {

    @Spy
    private EventStore eventStore = new InMemoryEventStore();
    @InjectMocks
    private GetAccountsQuery uut;

    @Test
    public void shouldGetAllAccounts() {
        final String expectedAccountName = "expectedAccountName";

        // given
        eventStore.storeEvent(
                Account.ACCOUNT_STREAM,
                AddAccountCommand.ADD_ACCOUNT_EVENT,
                AddAccount.builder()
                        .name(expectedAccountName)
                        .build()
        );

        // when
        final List<Account> accounts = uut.getAccounts();

        // then
        assertThat(accounts).isNotNull().isNotEmpty();
        assertThat(accounts).extracting("name").contains(expectedAccountName);
    }

}
