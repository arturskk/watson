package net.lipecki.watson.account;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.combiner.AggregateStreamCombiner;
import net.lipecki.watson.store.EventStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GetAccountsQuery {

    private final AggregateStreamCombiner<Account> combiner;

    public GetAccountsQuery(final EventStore eventStore) {
        this.combiner = new AggregateStreamCombiner<>(eventStore, Account.ACCOUNT_STREAM);
        this.combiner.registerHandler(
                AddAccountCommand.ADD_ACCOUNT_EVENT,
                (collection, event) -> collection.put(
                        event.getAggregateId(),
                        Account.builder()
                                .uuid(event.getAggregateId())
                                .name(event.castPayload(AddAccount.class).getName())
                                .build()
                )
        );
    }

    public List<Account> getAccounts() {
        return this.combiner.getAsList();
    }

}
