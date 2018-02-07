package net.lipecki.watson.productprice;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.product.ProductModified;
import net.lipecki.watson.projection.ProjectionHandler;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProductPriceProjectProductModified implements ProjectionHandler<ProductModified> {

    private final ProductPriceRepository repository;

    public ProductPriceProjectProductModified(final ProductPriceRepository repository) {
        this.repository = repository;
    }

    @Override
    public Class<ProductModified> getPayloadClass() {
        return ProductModified.class;
    }

    @Override
    public void accept(final Event event, final ProductModified payload) {
        payload.getCategoryUuidOptional()
                .ifPresent(
                        newCategory -> {
                            log.debug("Changing product category [uuid={}, newCategory={}]", payload.getUuid(), newCategory);
                            repository.updateProductCategory(payload.getUuid(), newCategory);
                        }
                );
    }

}
