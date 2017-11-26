package net.lipecki.watson.event;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.category.AddCategory;
import net.lipecki.watson.testing.BaseJpaTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ShouldPersistEventItTest extends BaseJpaTest {

    public static final String EVENT_STREAM = "stream";
    public static final String EVENT_TYPE = "type";
    @Autowired
    private EventStore uut;

    @Test
    public void shouldGetEmptyEvents() {
        // when
        final List<Event<?>> events = uut.getEvents().collect(Collectors.toList());

        // then
        assertThat(events).isEmpty();
    }

    @Test
    public void shouldPersistStringEvent() {
        final String expectedStream = "stream";
        final String expectedType = "type";
        final String expectedStringPayload = "payload";

        // given
        uut.storeEvent(expectedStream, expectedType, expectedStringPayload);

        // when
        final List<Event<?>> events = uut.getEvents().collect(Collectors.toList());

        // then
        assertThat(events).hasSize(1);
        final Event<?> event = events.get(0);
        assertThat(event.getStream()).isEqualTo(expectedStream);
        assertThat(event.getType()).isEqualTo(expectedType);
        assertThat(event.getPayload()).isEqualTo(expectedStringPayload);
    }

    @Test
    public void shouldPersistDtoObject() {
        final AddCategory payload = AddCategory.builder().name("category-name").type("category-type").build();

        // given
        uut.storeEvent(EVENT_STREAM, EVENT_TYPE, payload);

        // when
        final List<Event<?>> events = uut.getEvents().collect(Collectors.toList());

        // then
        assertThat(events).hasSize(1);
        final Event<?> event = events.get(0);
        assertThat(event.getPayload()).isEqualTo(payload);
    }
    
}
