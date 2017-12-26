package net.lipecki.watson.product;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.category.Category;
import net.lipecki.watson.category.GetCategoryQuery;
import net.lipecki.watson.combiner.AggregateCombinerHandler;
import net.lipecki.watson.event.Event;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class ProductAddedEventHandler implements AggregateCombinerHandler<Product, ProductAdded> {

    private final GetCategoryQuery categoryQuery;

    public ProductAddedEventHandler(final GetCategoryQuery categoryQuery) {
        this.categoryQuery = categoryQuery;
    }

    @Override
    public Class<ProductAdded> getPayloadClass() {
        return ProductAdded.class;
    }

    @Override
    public void accept(final Map<String, Product> collection, final Event event, final ProductAdded payload) {
        final Category category = payload
                .getCategoryUuidOptional()
                .flatMap(categoryQuery::getCategory)
                .orElseGet(categoryQuery::getRootCategory);

        collection.put(
                event.getStreamId(),
                Product.builder()
                        .uuid(event.getStreamId())
                        .name(payload.getName())
                        .category(category)
                        .build()
        );
    }

}
