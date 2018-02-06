package net.lipecki.watson.projection;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.event.EventStore;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Service
@Scope("prototype")
public class InMemoryProjectionThread extends BackgroundProjectionThread {

    private long lastSequenceId = 0;

    public InMemoryProjectionThread(
            final EventStore eventStore,
            final TaskScheduler taskScheduler,
            final PlatformTransactionManager platformTransactionManager) {
        super(eventStore, taskScheduler, platformTransactionManager);
    }

    @Override
    public long getCurrentProcessedSequenceId() {
        return this.lastSequenceId;
    }

    @Override
    public void setCurrentProcessedSequenceId(final long sequenceId) {
        this.lastSequenceId = sequenceId;
    }


}
