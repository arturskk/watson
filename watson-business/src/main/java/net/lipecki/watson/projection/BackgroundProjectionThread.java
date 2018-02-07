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
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

@Slf4j
@Scope("prototype")
public abstract class BackgroundProjectionThread implements ProjectionThread {

    private static final String THREAD_PREFIX = "ProjectionThread#%s-";

    private final EventStore eventStore;
    private final TaskScheduler taskScheduler;
    private final Map<String, ProjectionHandler<? extends EventPayload>> handlerMapping = new HashMap<>();
    private final TransactionTemplate transactionTemplate;
    private ThreadPoolTaskExecutor singleThreadTaskExecutor;

    private String id = "JpaProjectionThread";
    private boolean stable = false;
    private AtomicReference<ProjectionState> state = new AtomicReference<>(ProjectionState.STOPPED);

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
        handlerMapping.put(handler.getPayloadClass().getTypeName(), handler);
    }

    @Override
    public void setId(final String id) {
        this.id = id;
        singleThreadTaskExecutor.setThreadNamePrefix(String.format(THREAD_PREFIX, id));
    }

    @Override
    public void start() {
        taskScheduler.scheduleWithFixedDelay(
                () -> scheduleOperation(this::syncEvents),
                Instant.now().plusSeconds(10),
                Duration.ofSeconds(30)
        );
        state.set(ProjectionState.RUNNING);
    }

    @Override
    public ProjectionStatus getStatus() {
        return ProjectionStatus
                .builder()
                .stable(stable)
                .statusDate(new Date())
                .currentSequenceId(getCurrentProcessedSequenceId())
                .currentMaxSequenceId(eventStore.getLastSequenceId())
                .state(state.get())
                .build();
    }

    @Override
    public void pushEvent(final Event event) {
        stable = false;
        if (handlerMapping.containsKey(event.getType())) {
            log.debug("New event in system, schedule event sync");
            scheduleOperation(this::syncEvents);
        }
    }

    @Override
    public void resetProjection(final Runnable resetCallback) {
        transactionTemplate.execute(
                new TransactionCallbackWithoutResult() {
                    protected void doInTransactionWithoutResult(final TransactionStatus status) {
                        state.set(ProjectionState.STOPPED);
                        resetCallback.run();
                        setCurrentProcessedSequenceId(0L);
                        stable = false;
                        state.set(ProjectionState.RUNNING);
                    }
                }
        );
    }

    public String getId() {
        return id;
    }

    public abstract long getCurrentProcessedSequenceId();

    public abstract void setCurrentProcessedSequenceId(long sequenceId);

    private synchronized void syncEvents() {
        if (state.get() == ProjectionState.STOPPED) {
            return;
        }

        long startTime = System.currentTimeMillis();
        log.trace("[{}] Scheduler sync events task", id);
        final long lastSequenceId = getCurrentProcessedSequenceId();
        final AtomicReference<Long> lastProcessedEvendSequenceId = new AtomicReference<>();
        final Stream<Event> newEvents = eventStore.getEventsAfter(lastSequenceId, 500);
        newEvents.forEach(event -> {
            final long eventSequenceId = event.getSequenceId();
            final boolean eventNewestThanSynchronized = eventSequenceId > lastSequenceId;
            if (eventNewestThanSynchronized) {
                final String eventType = event.getType();
                final ProjectionHandler<? extends EventPayload> eventHandler = handlerMapping.get(eventType);
                if (eventHandler != null && eventHandler.canHandle(event)) {
                    try {
                        eventHandler.acceptWithCheck(event);
                    } catch (final Exception ex) {
                        log.warn("[{}] Event processing skipped due to handler exception [event={}]", id, event, ex);
                    }
                }
                lastProcessedEvendSequenceId.set(eventSequenceId);
                stable = eventSequenceId == eventStore.getLastSequenceId();
            }
        });
        if (lastProcessedEvendSequenceId.get() != null) {
            long endTime = System.currentTimeMillis();
            setCurrentProcessedSequenceId(lastProcessedEvendSequenceId.get());
            log.debug("[{}] Current sync status [time={}ms, status={}]", id, (endTime - startTime), getStatus());
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

    private void scheduleOperation(final Runnable runnable) {
        singleThreadTaskExecutor.execute(() -> transactionTemplate.execute(
                new TransactionCallbackWithoutResult() {
                    protected void doInTransactionWithoutResult(final TransactionStatus status) {
                        runnable.run();
                    }
                }
        ));
    }

}

