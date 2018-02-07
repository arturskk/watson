package net.lipecki.watson.projection;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class ProjectionThreadScheduler {

    private final TaskScheduler taskScheduler;
    private final List<ProjectionThread> projectionThreads;

    public ProjectionThreadScheduler(final TaskScheduler taskScheduler, final List<ProjectionThread> projectionThreads) {
        this.taskScheduler = taskScheduler;
        this.projectionThreads = projectionThreads;
    }

    @PostConstruct
    public void schedulerProjectionThreads() {
//        this.projectionThreads.forEach(
//                thread -> taskScheduler.scheduleWithFixedDelay(
//                        thread::projectionTick,
//                        Instant.now().plusSeconds(10),
//                        Duration.ofSeconds(10)
//                )
//        );
    }

}
