package net.lipecki.watson.product;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.category.GetCategoryQuery;
import net.lipecki.watson.event.Event;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.BiConsumer;

@Slf4j
@Service
public class ModifyProductEventHandler implements BiConsumer<Map<String, Product>, Event<?>> {

    private final GetCategoryQuery categoryQuery;

    public ModifyProductEventHandler(final GetCategoryQuery categoryQuery) {
        this.categoryQuery = categoryQuery;
    }

    @Override
    public void accept(final Map<String, Product> collection, final Event<?> event) {
        final ModifyProduct modifyProduct = event.castPayload(ModifyProduct.class);

        final Product product = collection.get(modifyProduct.getUuid());
        modifyProduct
                .getNameOptional()
                .ifPresent(product::setName);
        modifyProduct
                .getCategoryUuidOptional()
                .flatMap(categoryQuery::getCategory)
                .ifPresent(product::setCategory);
    }

}
