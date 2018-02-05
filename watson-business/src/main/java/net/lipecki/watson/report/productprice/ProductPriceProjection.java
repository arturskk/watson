package net.lipecki.watson.report.productprice;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.event.EventStore;
import net.lipecki.watson.product.ProductAdded;
import net.lipecki.watson.projection.JpaProjectionThread;
import net.lipecki.watson.projection.ProjectionHandler;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Service
public class ProductPriceProjection extends JpaProjectionThread {

    private final ProjectionHandler<ProductAdded> productAddedProjectionHandler;

    public ProductPriceProjection(final EventStore eventStore, final TaskScheduler taskScheduler, final PlatformTransactionManager platformTransactionManager) {
        super("ProductPriceProjection", eventStore, taskScheduler, platformTransactionManager);
        this.productAddedProjectionHandler = new ProjectionHandler<ProductAdded>() {

            @Override
            public Class<ProductAdded> getPayloadClass() {
                return ProductAdded.class;
            }

            @Override
            public void accept(final Event event, final ProductAdded payload) {
                log.info("ProductAdded event [event={}]", event);
            }

        };
        addHandler(productAddedProjectionHandler);
        setOnlyBackground(false);
    }

    @EventListener
    public void handleProductAdded(final Event event) {
        this.scheduleOperation(() -> {
            if (isStable() && event.getSequenceId() > this.getLastSequenceId()) {
                this.productAddedProjectionHandler.acceptWithCheck(event);
            }
        });
    }

}
