package net.lipecki.watson.projection;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.event.EventPayload;
import net.lipecki.watson.event.EventStore;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Slf4j
@Scope("prototype")
public abstract class BackgroundProjectionThread implements ProjectionThread {

    private static final String THREAD_PREFIX = "JpaProjectionThread#%s-";

    private final EventStore eventStore;
    private final TaskScheduler taskScheduler;
    private final Map<String, ProjectionHandler<? extends EventPayload>> handlerMapping = new HashMap<>();
    private final TransactionTemplate transactionTemplate;
    private ThreadPoolTaskExecutor singleThreadTaskExecutor;

    private String id = "JpaProjectionThread";
    private boolean stable = false;

    public BackgroundProjectionThread(
            final EventStore eventStore,
            final TaskScheduler taskScheduler,
            final PlatformTransactionManager platformTransactionManager) {
        this.eventStore = eventStore;
        this.taskScheduler = taskScheduler;
        this.singleThreadTaskExecutor = initializeSingleThreadedTaskExecutor(id);
        this.transactionTemplate = new TransactionTemplate(platformTransactionManager);
    }

    @Override
    public void addHandler(final ProjectionHandler<? extends EventPayload> handler) {
        this.handlerMapping.put(handler.getPayloadClass().getTypeName(), handler);
    }

    @Override
    public void setId(final String id) {
        this.id = id;
        this.singleThreadTaskExecutor.setThreadNamePrefix(String.format(THREAD_PREFIX, id));
    }

    @Override
    public void start() {
        taskScheduler.scheduleWithFixedDelay(
                () -> scheduleOperation(this::syncEvents),
                Instant.now().plusSeconds(10),
                Duration.ofSeconds(30)
        );
    }

    @Override
    public ProjectionStatus getStatus() {
        return ProjectionStatus
                .builder()
                .stable(this.stable)
                .statusDate(new Date())
                .currentSequenceId(getCurrentProcessedSequenceId())
                .currentMaxSequenceId(this.eventStore.getLastSequenceId())
                .build();
    }

    @Override
    public void pushEvent(final Event event) {
        this.stable = false;
        if (this.handlerMapping.containsKey(event.getType())) {
            log.debug("New event in system, schedule event sync");
            scheduleOperation(this::syncEvents);
        }
    }

    public String getId() {
        return id;
    }

    public abstract long getCurrentProcessedSequenceId();

    public abstract void setCurrentProcessedSequenceId(long sequenceId);

    private void syncEvents() {
        log.trace("[{}] Scheduler sync events task", this.id);
        final long lastSequenceId = getCurrentProcessedSequenceId();
        final Stream<Event> newEvents = this.eventStore.getEventsAfter(lastSequenceId, 200);
        newEvents.forEach(event -> {
            final long eventSequenceId = event.getSequenceId();
            final boolean eventNewestThanSynchronized = eventSequenceId > lastSequenceId;
            if (eventNewestThanSynchronized) {
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
                setCurrentProcessedSequenceId(eventSequenceId);
                this.stable = eventSequenceId == this.eventStore.getLastSequenceId();
                log.debug("[{}] Last processed event [sequenceId={}]", this.id, eventSequenceId);
            }
        });
        log.debug("[{}] Current sync status [status={}]", this.id, getStatus());
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

    private void scheduleOperation(final Runnable runnable) {
        this.singleThreadTaskExecutor.execute(() -> transactionTemplate.execute(
                new TransactionCallbackWithoutResult() {
                    protected void doInTransactionWithoutResult(final TransactionStatus status) {
                        runnable.run();
                    }
                }
        ));
    }

}

