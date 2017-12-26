package net.lipecki.watson.receipt;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.account.GetAccountQuery;
import net.lipecki.watson.category.GetCategoryQuery;
import net.lipecki.watson.combiner.AggregateCombinerHandler;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.expanse.ExpanseCost;
import net.lipecki.watson.product.GetProductQuery;
import net.lipecki.watson.shop.GetShopQuery;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReceiptAddedEventHandler implements AggregateCombinerHandler<Receipt, ReceiptAdded> {

    private final GetShopQuery shopQuery;
    private final GetAccountQuery accountQuery;
    private final GetProductQuery productsQuery;
    private final GetCategoryQuery categoryQuery;

    public ReceiptAddedEventHandler(
            final GetShopQuery shopQuery,
            final GetAccountQuery accountQuery,
            final GetProductQuery productsQuery,
            final GetCategoryQuery categoryQuery) {
        this.shopQuery = shopQuery;
        this.accountQuery = accountQuery;
        this.productsQuery = productsQuery;
        this.categoryQuery = categoryQuery;
    }

    @Override
    public Class<ReceiptAdded> getPayloadClass() {
        return ReceiptAdded.class;
    }

    @Override
    public void accept(final Map<String, Receipt> collection, final Event event, final ReceiptAdded payload) {
        final Receipt.ReceiptBuilder receipt = Receipt.builder();

        receipt.uuid(event.getStreamId());
        receipt.date(LocalDate.parse(payload.getDate()));
        shopQuery.getShop(payload.getShopUuid()).ifPresent(receipt::shop);
        accountQuery.getAccount(payload.getAccountUuid()).ifPresent(receipt::account);
        categoryQuery.getCategory(payload.getCategoryUuid()).ifPresent(receipt::category);
        receipt.items(payload.getItems().stream().map(this::asReceiptItem).collect(Collectors.toList()));

        collection.put(
                event.getStreamId(),
                receipt.build()
        );
    }

    private ReceiptItem asReceiptItem(final ReceiptItemAdded data) {
        final ReceiptItem.ReceiptItemBuilder item = ReceiptItem.builder();

        item.cost(ExpanseCost.of(data.getCost()));
        item.amount(
                ReceiptItemAmount
                        .builder()
                        .count(data.getAmount().getCount())
                        .unit(AmountUnit.getByAlias(data.getAmount().getUnit()).orElse(AmountUnit.UNKNOWN))
                        .build()
        );
        productsQuery.getProduct(data.getProductUuid()).ifPresent(item::product);

        return item.build();
    }

}
