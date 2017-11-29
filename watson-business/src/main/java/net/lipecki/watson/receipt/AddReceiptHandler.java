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
public class AddReceiptHandler implements AggregateCombinerHandler<Receipt> {

    private final GetShopQuery shopQuery;
    private final GetAccountQuery accountQuery;
    private final GetProductQuery productsQuery;
    private final GetCategoryQuery categoryQuery;

    public AddReceiptHandler(
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
    public void accept(final Map<String, Receipt> collection, final Event<?> event) {
        final AddReceipt addReceipt = event.castPayload(AddReceipt.class);

        final Receipt.ReceiptBuilder receipt = Receipt.builder();

        receipt.uuid(event.getStreamId());
        receipt.date(LocalDate.parse(addReceipt.getDate()));
        shopQuery.getShop(addReceipt.getShopUuid()).ifPresent(receipt::shop);
        accountQuery.getAccount(addReceipt.getAccountUuid()).ifPresent(receipt::account);
        categoryQuery.getCategory(addReceipt.getCategoryUuid()).ifPresent(receipt::category);
        receipt.items(addReceipt.getItems().stream().map(this::asReceiptItem).collect(Collectors.toList()));

        collection.put(
                event.getStreamId(),
                receipt.build()
        );
    }

    private ReceiptItem asReceiptItem(final AddReceiptItem dto) {
        final ReceiptItem.ReceiptItemBuilder item = ReceiptItem.builder();

        item.cost(ExpanseCost.of(dto.getCost()));
        item.amount(
                ReceiptItemAmount
                        .builder()
                        .count(dto.getAmount().getCount())
                        .unit(AmountUnit.getByAlias(dto.getAmount().getUnit()).orElse(AmountUnit.UNKNOWN))
                        .build()
        );
        productsQuery.getProduct(dto.getProduct().getUuid()).ifPresent(item::product);

        return item.build();
    }

}
