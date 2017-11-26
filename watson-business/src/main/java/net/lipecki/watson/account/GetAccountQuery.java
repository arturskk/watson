package net.lipecki.watson.account;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class GetAccountQuery {

    private final AccountStore accountStore;

    public GetAccountQuery(final AccountStore accountStore) {
        this.accountStore = accountStore;
    }

    public Optional<Account> getAccount(final String uuid) {
        return this.accountStore.getAccount(uuid);
    }

}
