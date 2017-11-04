package net.lipecki.watson.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventStoreConfiguration {

    @Bean
    public EventStore eventStore(final EventRepository eventRepository, final ObjectMapper objectMapper) {
        return new JpaEventStore(eventRepository, objectMapper);
        // return new InMemoryEventStore();
    }

}
