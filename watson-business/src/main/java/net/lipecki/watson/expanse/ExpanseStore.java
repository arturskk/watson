package net.lipecki.watson.expanse;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.WatsonException;
import net.lipecki.watson.combiner.AggregateCombiner;
import net.lipecki.watson.combiner.AggregateCombinerFactory;
import net.lipecki.watson.product.GetProductQuery;
import net.lipecki.watson.product.Product;
import net.lipecki.watson.receipt.AddReceipt;
import net.lipecki.watson.receipt.AddReceiptCommand;
import net.lipecki.watson.receipt.AddReceiptItem;
import net.lipecki.watson.receipt.Receipt;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
class ExpanseStore {

    private final AggregateCombiner<Expanse> combiner;

    public ExpanseStore(final AggregateCombinerFactory aggregateCombinerFactory, final GetProductQuery productQuery) {
        this.combiner = aggregateCombinerFactory.getAggregateCombiner(Collections.singletonList(Receipt.RECEIPT_STREAM));
        this.combiner.addHandler(
                AddReceiptCommand.ADD_RECEIPT_EVENT,
                (collection, event) -> {
                    final AddReceipt addReceipt = event.castPayload(AddReceipt.class);

                    final List<AddReceiptItem> items = addReceipt.getItems();
                    for (int itemIndex = 0; itemIndex < items.size(); ++itemIndex) {
                        final AddReceiptItem item = items.get(itemIndex);
                        final String itemKey = event.getStreamId() + "#" + itemIndex;

                        final Product product = productQuery
                                .getProduct(item.getProduct().getUuid())
                                .orElseThrow(() -> WatsonException.of("Missing product with item ref uuid").with("uuid", item.getProduct().getUuid()));

                        final Expanse.ExpanseBuilder expanse = Expanse.builder();
                        expanse.type(Product.PRODUCT_STREAM);
                        expanse.refUuid(itemKey);
                        expanse.date(LocalDate.parse(addReceipt.getDate()));
                        expanse.name(product.getName());
                        expanse.category(product.getCategory());
                        expanse.cost(ExpanseCost.of(item.getCost()));

                        collection.put(itemKey, expanse.build());
                    }
                }
        );
    }

    public List<Expanse> getExpanses(final LocalDate from, final LocalDate to) {
        return this.combiner
                .getAsList()
                .stream()
                .filter(expanse -> from == null || !expanse.getDate().isBefore(from))
                .filter(expanse -> to == null || !expanse.getDate().isAfter(to))
                .collect(Collectors.toList());
    }

}
