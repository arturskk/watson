package net.lipecki.watson.product;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.category.GetCategoryQuery;
import net.lipecki.watson.combiner.AggregateCombinerHandler;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.amount.AmountUnit;
import net.lipecki.watson.producer.GetProducerQuery;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class ProductModifiedEventHandler implements AggregateCombinerHandler<Product, ProductModified> {

    private final GetCategoryQuery categoryQuery;
    private final GetProducerQuery producerQuery;

    public ProductModifiedEventHandler(final GetCategoryQuery categoryQuery, final GetProducerQuery producerQuery) {
        this.categoryQuery = categoryQuery;
        this.producerQuery = producerQuery;
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
        payload
                .getProducerUuidOptional()
                .flatMap(producerQuery::getProducer)
                .ifPresent(product::setProducer);
        payload
                .getDefaultUnitOptional()
                .map(AmountUnit::getByAlias)
                .ifPresent(product::setDefaultUnit);
    }

}
