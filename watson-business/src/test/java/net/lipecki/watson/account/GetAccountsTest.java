package net.lipecki.watson.account;

import net.lipecki.watson.event.EventStore;
import net.lipecki.watson.event.InMemoryEventStore;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class GetAccountsTest {

    private EventStore eventStore;
    private GetAccountsQuery uut;

    @Before
    public void setUp() {
        this.eventStore = new InMemoryEventStore();
        this.uut = new GetAccountsQuery(eventStore);
    }

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
