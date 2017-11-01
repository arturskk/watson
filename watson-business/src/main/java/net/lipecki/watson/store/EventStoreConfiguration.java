package net.lipecki.watson.store;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventStoreConfiguration {

    @Bean
    public EventStore eventStore() {
        return new InMemoryEventStore();
    }

}
