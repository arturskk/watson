package net.lipecki.watson.productprice;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.amount.AmountUnit;
import net.lipecki.watson.cost.Cost;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.product.GetProductQuery;
import net.lipecki.watson.projection.ProjectionHandler;
import net.lipecki.watson.receipt.AddReceiptItemAmount;
import net.lipecki.watson.receipt.ReceiptAdded;
import net.lipecki.watson.receipt.ReceiptItemAdded;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ProductPriceProjectReceiptAdded implements ProjectionHandler<ReceiptAdded> {

    private final GetProductQuery productQuery;
    private final ProductPriceRepository repository;

    public ProductPriceProjectReceiptAdded(final GetProductQuery productQuery, final ProductPriceRepository repository) {
        this.productQuery = productQuery;
        this.repository = repository;
    }

    @Override
    public Class<ReceiptAdded> getPayloadClass() {
        return ReceiptAdded.class;
    }

    @Override
    public void accept(final Event event, final ReceiptAdded payload) {
        log.debug("Adding new entries after receipt added [receipt={}]", payload);
        final List<ProductPriceEntity> entities = new ArrayList<>();
        final List<ReceiptItemAdded> items = payload.getItems();
        for (int idx = 0; idx < items.size(); ++idx) {
            final ReceiptItemAdded item = items.get(idx);
            try {
                entities.add(
                        ProductPriceEntity
                                .builder()
                                .productUuid(item.getProductUuid())
                                .categoryUuid(productQuery.getProduct(item.getProductUuid()).get().getCategory().getUuid())
                                .shopUuid(payload.getShopUuid())
                                .receiptUuid(event.getStreamId())
                                .receiptItemUuid(event.getStreamId() + "$" + idx)
                                .date(payload.getDate())
                                .unit(item.getAmount().getUnit())
                                .pricePerUnit(getAsPricePerUnit(item.getCost(), item.getAmount()))
                                .build()
                );
            } catch (final Exception ex) {
                log.warn("Can't parse receipt item, skipping [item={}]", item, ex);
            }
        }
        entities.forEach(repository::save);
    }

    private String getAsPricePerUnit(final String rawCost, final AddReceiptItemAmount amount) {
        final Cost cost = Cost.of(rawCost);
        final AmountUnit unit = AmountUnit.getByAlias(amount.getUnit());
        switch (unit) {
            case KG:
            case L:
                return Cost.of(Math.round(cost.getAmount() / Double.parseDouble(amount.getCount()))).getDescription();
            case UNIT:
            case PACKAGE:
                return Cost.of(cost.getAmount() / Integer.parseInt(amount.getCount())).getDescription();
            case UNKNOWN:
            default:
                return "n/a";
        }
    }

}
