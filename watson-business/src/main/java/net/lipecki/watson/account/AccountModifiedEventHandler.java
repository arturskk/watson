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
    }

}
