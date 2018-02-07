package net.lipecki.watson.projection.jpa;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.event.EventStore;
import net.lipecki.watson.projection.BackgroundProjectionThread;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@Service
@Scope("prototype")
public class JpaProjectionThread extends BackgroundProjectionThread {

    private final JpaProjectionRepository repository;

    public JpaProjectionThread(
            final EventStore eventStore,
            final TaskScheduler taskScheduler,
            final PlatformTransactionManager platformTransactionManager,
            final JpaProjectionRepository repository) {
        super(eventStore, taskScheduler, platformTransactionManager);
        this.repository = repository;
    }

    @Transactional
    @Override
    public long getCurrentProcessedSequenceId() {
        final Optional<JpaProjectionThreadStatus> projectionThreadStatus = repository.findOneById(getId());
        return projectionThreadStatus.map(JpaProjectionThreadStatus::getLastSequenceId).orElse(0L);
    }

    @Transactional
    @Override
    public void setCurrentProcessedSequenceId(final long sequenceId) {
        repository.save(
                JpaProjectionThreadStatus
                        .builder()
                        .id(getId())
                        .lastSequenceId(sequenceId)
                        .build()
        );
    }

}
