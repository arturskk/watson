package net.lipecki.watson.testing;

import net.lipecki.watson.BaseItTest;
import net.lipecki.watson.TestEventStore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test event event should be cleared before each test.
 * We run two tests, each checks if event event is empty and adds new event.
 */
public class ShouldResetTestEventStoreItTest extends BaseItTest {

    @Autowired
    private TestEventStore testEventStore;

    @Test
    public void shouldResetEventStore1() {
        assertThat(testEventStore.getAllEvents()).isEmpty();

        testEventStore.storeEvent("any", "any");
    }

    @Test
    public void shouldResetEventStore2() {
        assertThat(testEventStore.getAllEvents()).isEmpty();

        testEventStore.storeEvent("any", "any");
    }

}
