package net.lipecki.watson.product;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.category.GetCategoryQuery;
import net.lipecki.watson.combiner.AggregateCombinerHandler;
import net.lipecki.watson.event.Event;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class ProductModifiedEventHandler implements AggregateCombinerHandler<Product, ProductModified> {

    private final GetCategoryQuery categoryQuery;

    public ProductModifiedEventHandler(final GetCategoryQuery categoryQuery) {
        this.categoryQuery = categoryQuery;
    }

    @Override
    public Class<ProductModified> getPayloadClass() {
        return ProductModified.class;
    }

    @Override
    public void accept(final Map<String, Product> collection, final Event event, final ProductModified payload) {
        final Product product = collection.get(payload.getUuid());
        payload
                .getNameOptional()
                .ifPresent(product::setName);
        payload
                .getCategoryUuidOptional()
                .flatMap(categoryQuery::getCategory)
                .ifPresent(product::setCategory);
    }

}
