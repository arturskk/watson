package net.lipecki.watson.account;

import net.lipecki.watson.combiner.AggregateCombinerHandler;
import net.lipecki.watson.event.Event;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AccountAddedEventHandler implements AggregateCombinerHandler<Account, AccountAdded> {

    @Override
    public Class<AccountAdded> getPayloadClass() {
        return AccountAdded.class;
    }

    @Override
    public void accept(final Map<String, Account> collection, final Event event, final AccountAdded payload) {
        if (Boolean.TRUE.equals(payload.getUseDefault())) {
            collection.values()
                    .stream()
                    .filter(acc ->  Boolean.TRUE.equals(acc.getUseDefault()))
                    .forEach(acc -> acc.setUseDefault(false));
        }
        collection.put(
                event.getStreamId(),
                Account.builder()
                        .uuid(event.getStreamId())
                        .name(payload.getName())
                        .useDefault(payload.getUseDefault())
                        .build()
        );
    }

}
