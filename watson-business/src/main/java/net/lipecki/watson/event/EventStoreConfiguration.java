package net.lipecki.watson.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventStoreConfiguration {

    @Bean
    public EventStore eventStore(
            final EventRepository eventRepository,
            final ObjectMapper objectMapper,
            final ApplicationEventPublisher applicationEventPublisher) {
        return new PublishingEventStoreDecorator(
                new JpaEventStore(eventRepository, objectMapper),
                applicationEventPublisher
        );
    }

}
