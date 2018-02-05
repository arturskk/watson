package net.lipecki.watson.projection;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.WatsonException;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.event.EventPayload;
import net.lipecki.watson.event.EventStore;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Slf4j
public abstract class JpaProjectionThread implements ProjectionThread {

    private static final String THREAD_PREFIX = "JpaProjectionThread#%s-";

    private final String id;
    private final EventStore eventStore;
    private final Map<String, ProjectionHandler<? extends EventPayload>> handlerMapping = new HashMap<>();
    private final TransactionTemplate transactionTemplate;
    private final TaskScheduler taskScheduler;
    private ThreadPoolTaskExecutor singleThreadTaskExecutor;

    private long lastSequenceId = 0;
    private boolean stable = false;
    private boolean onlyBackground = true;

    public JpaProjectionThread(
            final String id,
            final EventStore eventStore,
            final TaskScheduler taskScheduler,
            final PlatformTransactionManager platformTransactionManager) {
        this.id = id;
        this.eventStore = eventStore;
        this.singleThreadTaskExecutor = initializeSingleThreadedTaskExecutor(id);
        this.taskScheduler = taskScheduler;
        this.transactionTemplate = new TransactionTemplate(platformTransactionManager);
    }

    @Override
    public void addHandler(final ProjectionHandler<? extends EventPayload> handler) {
        this.handlerMapping.put(handler.getPayloadClass().getTypeName(), handler);
    }

    public void setOnlyBackground(final boolean onlyBackground) {
        this.onlyBackground = onlyBackground;
    }

    public boolean isStable() {
        return stable;
    }

    @PostConstruct
    private void scheduleProjectionTicks() {
        taskScheduler.scheduleWithFixedDelay(
                () -> scheduleOperation(this::syncEvents, true),
                Instant.now().plusSeconds(10),
                Duration.ofSeconds(30)
        );
    }

    protected void scheduleOperation(final Runnable runnable) {
        scheduleOperation(runnable, false);
    }

    protected long getLastSequenceId() {
        return this.lastSequenceId;
    }

    private void syncEvents() {
        final boolean shouldSyncBatch = !this.stable || this.onlyBackground;
        if (shouldSyncBatch) {
            log.trace("[{}] Scheduler projectionTick", this.id);
            final Stream<Event> newEvents = this.eventStore.getEventsAfter(this.lastSequenceId, 200);
            newEvents.forEach(event -> {
                final boolean eventNewestThanSynchronized = event.getSequenceId() > this.getLastSequenceId();
                if (shouldSyncBatch && eventNewestThanSynchronized) {
                    final String eventType = event.getType();
                    final ProjectionHandler<? extends EventPayload> eventHandler = this.handlerMapping.get(eventType);
                    if (eventHandler != null && eventHandler.canHandle(event)) {
                        try {
                            log.trace("[{}] New event to consume for projection thread [event={}]", this.id, event);
                            eventHandler.acceptWithCheck(event);
                        } catch (final Exception ex) {
                            log.warn("[{}] Event processing skipped due to handler exception [event={}]", this.id, event, ex);
                        }
                    }
                    this.lastSequenceId = event.getSequenceId();
                    this.stable = this.lastSequenceId == this.eventStore.getLastSequenceId();
                    log.debug("[{}] Last processed event [sequenceId={}, stable={}]", this.id, this.lastSequenceId, this.stable);
                }
            });
        }
    }

    private ThreadPoolTaskExecutor initializeSingleThreadedTaskExecutor(final String id) {
        final ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(1);
        taskExecutor.setThreadNamePrefix(String.format(THREAD_PREFIX, id));
        taskExecutor.setThreadGroupName(getClass().getSimpleName());
        taskExecutor.setMaxPoolSize(1);
        taskExecutor.initialize();
        return taskExecutor;
    }

    private void scheduleOperation(final Runnable runnable, final boolean background) {
        if (onlyBackground && !background) {
            throw WatsonException
                    .of("Cam't schedule foreground task for background projection thread")
                    .with("projectionThreadId", this.id);
        }
        this.singleThreadTaskExecutor.execute(() -> transactionTemplate.execute(
                new TransactionCallbackWithoutResult() {
                    protected void doInTransactionWithoutResult(final TransactionStatus status) {
                        runnable.run();
                    }
                }
        ));
    }

}
