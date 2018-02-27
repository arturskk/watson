package net.lipecki.watson.receipt;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.account.GetAccountQuery;
import net.lipecki.watson.amount.AmountUnit;
import net.lipecki.watson.category.GetCategoryQuery;
import net.lipecki.watson.combiner.AggregateCombinerHandler;
import net.lipecki.watson.cost.Cost;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.product.GetProductQuery;
import net.lipecki.watson.shop.GetShopQuery;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        receipt.items(getReceiptItems(payload.getSchemaVersion(), payload.getItems(), event.getStreamId()));

        collection.put(
                event.getStreamId(),
                receipt.build()
        );
    }

    private List<ReceiptItem> getReceiptItems(final long schemaVersion, final List<ReceiptItemAdded> items, final String receiptUuid) {
        final List<ReceiptItem> result = new ArrayList<>();
        for (final ReceiptItemAdded item : items) {
            final int index = items.indexOf(item);
            result.add(asReceiptItem(schemaVersion, item, receiptUuid, index));
        }
        return result;
    }

    private ReceiptItem asReceiptItem(final long schemaVersion, final ReceiptItemAdded data, final String receiptUuid, final int index) {
        final ReceiptItem.ReceiptItemBuilder item = ReceiptItem.builder();

        if (schemaVersion < 2) {
            item.uuid(ReceiptItemAdded.combineUuidBasedOnIndex(receiptUuid, index));
        } else {
            item.uuid(data.getUuid());
        }
        item.cost(Cost.of(data.getCost()));
        item.amount(
                ReceiptItemAmount
                        .builder()
                        .count(data.getAmount().getCount())
                        .unit(AmountUnit.getByAlias(data.getAmount().getUnit()))
                        .build()
        );
        productsQuery.getProduct(data.getProductUuid()).ifPresent(item::product);

        return item.build();
    }

}
