package net.lipecki.watson.account;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.combiner.AggregateCombiner;
import net.lipecki.watson.combiner.AggregateCombinerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
class AccountStore {

    private final AggregateCombiner<Account> combiner;

    public AccountStore(
            final AggregateCombinerFactory aggregateCombinerFactory,
            final AccountAddedEventHandler accountAddedEventHandler,
            final AccountModifiedEventHandler accountModifiedEventHandler) {
        this.combiner = aggregateCombinerFactory.getAggregateCombiner(getClass().getSimpleName(), Collections.singletonList(Account.ACCOUNT_STREAM));
        combiner.addHandler(accountAddedEventHandler);
        combiner.addHandler(accountModifiedEventHandler);
    }

    public List<Account> getAccounts() {
        return this.combiner.getAsList();
    }

    public Optional<Account> getAccount(final String uuid) {
        return Optional.ofNullable(
                this.combiner.get().get(uuid)
        );
    }

}
