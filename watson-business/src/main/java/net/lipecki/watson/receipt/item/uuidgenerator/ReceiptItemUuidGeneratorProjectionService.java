package net.lipecki.watson.receipt.item.uuidgenerator;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.projection.ProjectionStatus;
import net.lipecki.watson.projection.ProjectionThread;
import net.lipecki.watson.projection.jpa.JpaProjectionThread;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class ReceiptItemUuidGeneratorProjectionService {

    private final ProjectionThread projectionThread;

    public ReceiptItemUuidGeneratorProjectionService(final JpaProjectionThread projectionThread, final GenerateItemUuidForReceiptAdded receiptAdded) {
        this.projectionThread = projectionThread;
        projectionThread.addHandler(receiptAdded);
        projectionThread.setId(getClass().getSimpleName());
    }

    @PostConstruct
    public void startProjectionThread() {
        projectionThread.start();
    }

    @EventListener
    public void onEventAdded(final Event event) {
        projectionThread.pushEvent(event);
    }

    public ProjectionStatus getProjectionStatus() {
        return projectionThread.getStatus();
    }

}
