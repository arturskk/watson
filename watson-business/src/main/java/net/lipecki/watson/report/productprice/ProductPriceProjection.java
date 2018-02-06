package net.lipecki.watson.report.productprice;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.product.ProductAdded;
import net.lipecki.watson.projection.ProjectionHandler;
import net.lipecki.watson.projection.ProjectionThread;
import net.lipecki.watson.projection.jpa.JpaProjectionThread;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class ProductPriceProjection {

    private final ProjectionThread projectionThread;

    public ProductPriceProjection(final JpaProjectionThread projectionThread) {
        this.projectionThread = projectionThread;
        this.projectionThread.setId("projection-price");
        this.projectionThread.addHandler(new ProjectionHandler<ProductAdded>() {

            @Override
            public Class<ProductAdded> getPayloadClass() {
                return ProductAdded.class;
            }

            @Override
            public void accept(final Event event, final ProductAdded payload) {
                log.info("ProductAdded event [event={}]", event);
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

}
