package net.lipecki.watson.expanse;

import net.lipecki.watson.WatsonException;
import net.lipecki.watson.combiner.AggregateCombinerHandler;
import net.lipecki.watson.cost.Cost;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.product.GetProductQuery;
import net.lipecki.watson.product.Product;
import net.lipecki.watson.receipt.ReceiptAdded;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        payload.getItems()
                .stream()
                .map(
                        item -> {
                            final Product product = productQuery
                                    .getProduct(item.getProductUuid())
                                    .orElseThrow(() -> WatsonException.of("Missing product with item ref uuid").with("uuid", item.getProductUuid()));
                            return Expanse.builder()
                                    .type(Product.PRODUCT_STREAM)
                                    .refUuid(item.getUuid())
                                    .date(LocalDate.parse(payload.getDate()))
                                    .name(product.getName())
                                    .category(product.getCategory())
                                    .cost(Cost.of(item.getCost()))
                                    .build();
                        }
                )
                .forEach(item -> collection.put(item.getRefUuid(), item));
    }

}
