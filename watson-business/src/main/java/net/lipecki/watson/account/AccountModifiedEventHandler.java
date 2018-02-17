package net.lipecki.watson.account;

import net.lipecki.watson.combiner.AggregateCombinerHandler;
import net.lipecki.watson.event.Event;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AccountModifiedEventHandler implements AggregateCombinerHandler<Account, AccountModified> {

    @Override
    public Class<AccountModified> getPayloadClass() {
        return AccountModified.class;
    }

    @Override
    public void accept(final Map<String, Account> collection, final Event event, final AccountModified payload) {
        final Account account = collection.get(payload.getUuid());
        payload
                .getNameOptional()
                .ifPresent(account::setName);
        payload
                .getUseDefaultOptional()
                .filter(useDefault -> !useDefault.equals(account.getUseDefault()))
                .ifPresent(useDefault -> switchDefaultAccount(collection, account, useDefault));
    }

    private void switchDefaultAccount(final Map<String, Account> collection, final Account account, final Boolean useDefault) {
        if (useDefault) {
            collection.values()
                    .stream()
                    .filter(acc -> Boolean.TRUE.equals(acc.getUseDefault()))
                    .forEach(acc -> acc.setUseDefault(false));
        }
        account.setUseDefault(useDefault);
    }

}
