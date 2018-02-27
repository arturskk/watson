package net.lipecki.watson.expanse;

import net.lipecki.watson.WatsonException;
import net.lipecki.watson.combiner.AggregateCombinerHandler;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.cost.Cost;
import net.lipecki.watson.product.GetProductQuery;
import net.lipecki.watson.product.Product;
import net.lipecki.watson.receipt.ReceiptAdded;
import net.lipecki.watson.receipt.ReceiptItemAdded;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class ExpanseReceiptAddedEventHandler implements AggregateCombinerHandler<Expanse, ReceiptAdded> {

    private final GetProductQuery productQuery;

    public ExpanseReceiptAddedEventHandler(final GetProductQuery productQuery) {
        this.productQuery = productQuery;
    }

    @Override
    public Class<ReceiptAdded> getPayloadClass() {
        return ReceiptAdded.class;
    }

    @Override
    public void accept(final Map<String, Expanse> collection, final Event event, final ReceiptAdded payload) {
        final List<ReceiptItemAdded> items = payload.getItems();
        for (int itemIndex = 0; itemIndex < items.size(); ++itemIndex) {
            final ReceiptItemAdded item = items.get(itemIndex);

            final Product product = productQuery
                    .getProduct(item.getProductUuid())
                    .orElseThrow(() -> WatsonException.of("Missing product with item ref uuid").with("uuid", item.getProductUuid()));

            final Expanse.ExpanseBuilder expanseBuilder = Expanse.builder();
            expanseBuilder.type(Product.PRODUCT_STREAM);
            if (payload.getSchemaVersion() < ReceiptAdded.SchemaVersion.V2) {
                expanseBuilder.refUuid(ReceiptAdded.combineItemUuid(event.getStreamId(), itemIndex));
            } else {
                expanseBuilder.refUuid(item.getUuid());
            }
            expanseBuilder.date(LocalDate.parse(payload.getDate()));
            expanseBuilder.name(product.getName());
            expanseBuilder.category(product.getCategory());
            expanseBuilder.cost(Cost.of(item.getCost()));

            final Expanse expanse = expanseBuilder.build();
            collection.put(expanse.getRefUuid(), expanse);
        }
    }

}
