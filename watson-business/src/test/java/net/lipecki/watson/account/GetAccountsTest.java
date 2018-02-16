package net.lipecki.watson.account;

import net.lipecki.watson.combiner.TestAggregateCombinerWithCacheFactory;
import net.lipecki.watson.event.EventStore;
import net.lipecki.watson.event.InMemoryEventStore;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class GetAccountsTest {

    private EventStore eventStore;
    private GetAccountsQuery uut;

    @Before
    public void setUp() {
        this.eventStore = new InMemoryEventStore();
        this.uut = new GetAccountsQuery(
                new AccountStore(
                        TestAggregateCombinerWithCacheFactory.of(eventStore),
                        new AccountAddedEventHandler(),
                        new AccountModifiedEventHandler()
                )
        );
    }

    @Test
    public void shouldGetAllAccounts() {
        final String expectedAccountName = "expectedAccountName";

        // given
        eventStore.storeEvent(
                Account.ACCOUNT_STREAM,
                AccountAdded
                        .builder()
                        .name(expectedAccountName)
                        .build()
        );

        // when
        final List<Account> accounts = uut.getAccounts();

        // then
        assertThat(accounts).isNotNull().isNotEmpty();
        assertThat(accounts).extracting(Account::getName).contains(expectedAccountName);
    }

}
