package net.lipecki.watson.account;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GetAccountsQuery {

    private final AccountStore accountStore;

    public GetAccountsQuery(final AccountStore accountStore) {
        this.accountStore = accountStore;
    }

    public List<Account> getAccounts() {
        return this.accountStore
                .getAccounts()
                .stream()
                .sorted(Comparator.comparing(Account::getName, Collator.getInstance(Locale.forLanguageTag("pl"))))
                .collect(Collectors.toList());
    }

}
