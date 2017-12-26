package net.lipecki.watson.event;

import lombok.Builder;
import lombok.Data;
import net.lipecki.watson.testing.BaseJpaTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class GetEventByStreamItTest extends BaseJpaTest {

    @Builder
    @Data
    private static class TestEventPayload implements EventPayload {

        private String data;

    }

    private static final String EVENT_STREAM = "stream";
    private static final String EVENT_OTHER_STREAM = "other-stream";
    @Autowired
    private EventStore uut;

    @Test
    public void shouldGetEventByStream() {
        final TestEventPayload expectedPayload = TestEventPayload.builder().data("expected-payload").build();

        // given
        uut.storeEvent(EVENT_OTHER_STREAM, TestEventPayload.builder().data("unexpected-payload").build());
        uut.storeEvent(EVENT_STREAM, expectedPayload);

        // when
        final List<Event> events = uut.getEventsByStream(Collections.singletonList(EVENT_STREAM)).collect(Collectors.toList());

        // then
        assertThat(events)
                .extracting(item -> item.getPayload())
                .containsExactly(expectedPayload);
    }

}
