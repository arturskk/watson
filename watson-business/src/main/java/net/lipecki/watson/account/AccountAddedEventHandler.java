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
        collection.put(
                event.getStreamId(),
                Account.builder()
                        .uuid(event.getStreamId())
                        .name(payload.getName())
                        .build()
        );
    }

}
