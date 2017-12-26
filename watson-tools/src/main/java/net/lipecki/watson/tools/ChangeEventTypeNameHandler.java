package net.lipecki.watson.tools;

import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class ChangeEventTypeNameHandler implements EventsTableRowHandler {

    public static final String COLUMN_TYPE = "type";
    private Map<String, String> typeToTypeMapper = new HashMap<>();

    public ChangeEventTypeNameHandler() {
        typeToTypeMapper.put("_account_add", "net.lipecki.watson.account.AccountAdded");
        typeToTypeMapper.put("_category_add", "net.lipecki.watson.category.CategoryAdded");
        typeToTypeMapper.put("_category_modify", "net.lipecki.watson.category.CategoryModified");
        typeToTypeMapper.put("_product_add", "net.lipecki.watson.product.ProductAdded");
        typeToTypeMapper.put("_product_modify", "net.lipecki.watson.product.ProductModified");
        typeToTypeMapper.put("_receipt_add", "net.lipecki.watson.receipt.ReceiptAdded");
        typeToTypeMapper.put("_shop_add", "net.lipecki.watson.shop.ShopAdded");
    }

    @Override
    public boolean isApplicable(final ResultSet eventRow) throws Exception {
        final String type = eventRow.getString(COLUMN_TYPE);
        boolean isApplicable = Objects.nonNull(type) && this.typeToTypeMapper.containsKey(type);
        if (!isApplicable) {
            log.warn("Not applicable event type, skipped! [type={}]", type);
        }
        return isApplicable;
    }

    @Override
    public void handle(final ResultSet eventRow, final boolean dryRun) throws Exception {
        final String oldType = eventRow.getString(COLUMN_TYPE);
        final String newType = this.typeToTypeMapper.get(oldType);
        log.info("Migrating event type [from={}, to={}]", oldType, newType);
        if (!dryRun) {
            eventRow.updateString(COLUMN_TYPE, newType);
        }
    }

}
