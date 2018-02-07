package net.lipecki.watson.productprice;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.projection.ProjectionStatus;
import net.lipecki.watson.projection.ProjectionThread;
import net.lipecki.watson.projection.jpa.JpaProjectionThread;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class ProductPriceProjectionService {

    private final ProjectionThread projectionThread;
    private final ProductPriceRepository repository;

    public ProductPriceProjectionService(
            final JpaProjectionThread projectionThread,
            final ProductPriceRepository repository,
            final ProductPriceProjectReceiptAdded productPriceProjectReceiptAdded,
            final ProductPriceProjectProductModified productPriceProjectProductModified) {
        this.projectionThread = projectionThread;
        this.repository = repository;
        projectionThread.setId(getClass().getSimpleName());
        projectionThread.addHandler(productPriceProjectReceiptAdded);
        projectionThread.addHandler(productPriceProjectProductModified);
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

    @Transactional
    public ProjectionStatus resetProjection() {
        projectionThread.resetProjection(
                () -> repository.deleteAll()
        );
        return projectionThread.getStatus();
    }

}
