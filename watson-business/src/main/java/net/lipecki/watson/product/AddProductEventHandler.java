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
public class AddProductEventHandler implements AggregateCombinerHandler<Product> {

    private final GetCategoryQuery categoryQuery;

    public AddProductEventHandler(final GetCategoryQuery categoryQuery) {
        this.categoryQuery = categoryQuery;
    }

    @Override
    public void accept(final Map<String, Product> collection, final Event<?> event) {
        final AddProduct addProduct = event.castPayload(AddProduct.class);

        final Category category = addProduct
                .getCategoryUuidOptional()
                .flatMap(categoryQuery::getCategory)
                .orElseGet(categoryQuery::getRootCategory);

        collection.put(
                event.getStreamId(),
                Product.builder()
                        .uuid(event.getStreamId())
                        .name(addProduct.getName())
                        .category(category)
                        .build()
        );
    }

}
