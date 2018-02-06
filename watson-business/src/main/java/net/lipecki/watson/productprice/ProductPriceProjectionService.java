package net.lipecki.watson.productprice;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.projection.ProjectionHandler;
import net.lipecki.watson.projection.ProjectionStatus;
import net.lipecki.watson.projection.ProjectionThread;
import net.lipecki.watson.projection.jpa.JpaProjectionThread;
import net.lipecki.watson.receipt.ReceiptAdded;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class ProductPriceProjectionService {

    private final ProjectionThread projectionThread;
    private final ProductPriceRepository repository;

    public ProductPriceProjectionService(final JpaProjectionThread projectionThread, final ProductPriceRepository repository) {
        this.projectionThread = projectionThread;
        this.repository = repository;
        this.projectionThread.setId(getClass().getSimpleName());
        this.projectionThread.addHandler(new ProjectionHandler<ReceiptAdded>() {

            @Override
            public Class<ReceiptAdded> getPayloadClass() {
                return ReceiptAdded.class;
            }

            @Override
            public void accept(final Event event, final ReceiptAdded payload) {
                log.info("ReceiptAdded event [event={}]", event);
            }

        });
    }

    @PostConstruct
    public void startProjectionThread() {
        this.projectionThread.start();
    }

    @EventListener
    public void onEventAdded(final Event event) {
        this.projectionThread.pushEvent(event);
    }

    public ProjectionStatus getProjectionStatus() {
        return this.projectionThread.getStatus();
    }

    @Transactional
    public ProjectionStatus resetProjection() {
        this.projectionThread.resetProjection(
                () -> this.repository.deleteAll()
        );
        return this.projectionThread.getStatus();
    }

}
