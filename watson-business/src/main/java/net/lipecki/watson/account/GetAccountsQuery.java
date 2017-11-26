package net.lipecki.watson.account;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GetAccountsQuery {

    private final AccountStore accountStore;

    public GetAccountsQuery(final AccountStore accountStore) {
        this.accountStore = accountStore;
    }

    public List<Account> getAccounts() {
        return this.accountStore.getAccounts();
    }

}
