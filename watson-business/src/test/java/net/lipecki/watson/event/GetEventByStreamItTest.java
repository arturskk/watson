package net.lipecki.watson.event;

import net.lipecki.watson.testing.BaseJpaTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GetEventByStreamItTest extends BaseJpaTest {

    public static final String EVENT_STREAM = "stream";
    public static final String EVENT_OTHER_STREAM = "other-stream";
    public static final String EVENT_TYPE = "type";
    @Autowired
    private EventStore uut;

    @Test
    public void shouldGetEventByStream() {
        final String expectedPayload = "expected-payload";

        // given
        uut.storeEvent(EVENT_OTHER_STREAM, EVENT_TYPE, "unexpected-payload");
        uut.storeEvent(EVENT_STREAM, EVENT_TYPE, expectedPayload);

        // when
        final List<Event<?>> events = uut.getEventsByStream(EVENT_STREAM);

        // then
        assertThat(events).extracting("payload").containsExactly(expectedPayload);
    }

}
