package net.lipecki.watson.tools;

import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class ChangePayloadClassHandler implements EventsTableRowHandler {

    public static final String COLUMN_PAYLOAD_CLASS = "payload_class";
    private Map<String, String> classToClassMapper = new HashMap<>();

    public ChangePayloadClassHandler() {
        classToClassMapper.put("net.lipecki.watson.account.AddAccount", "net.lipecki.watson.account.AccountAdded");
        classToClassMapper.put("net.lipecki.watson.category.AddCategory", "net.lipecki.watson.category.CategoryAdded");
        classToClassMapper.put("net.lipecki.watson.category.ModifyCategory", "net.lipecki.watson.category.CategoryModified");
        classToClassMapper.put("net.lipecki.watson.product.AddProduct", "net.lipecki.watson.product.ProductAdded");
        classToClassMapper.put("net.lipecki.watson.product.ModifyProduct", "net.lipecki.watson.product.ProductModified");
        classToClassMapper.put("net.lipecki.watson.receipt.AddReceipt", "net.lipecki.watson.receipt.ReceiptAdded");
        classToClassMapper.put("net.lipecki.watson.shop.AddShop", "net.lipecki.watson.shop.ShopAdded");
    }

    @Override
    public boolean isApplicable(final ResultSet eventRow) throws Exception {
        final String payloadClass = eventRow.getString(COLUMN_PAYLOAD_CLASS);
        boolean isApplicable = Objects.nonNull(payloadClass) && this.classToClassMapper.containsKey(payloadClass);
        if (!isApplicable) {
            log.warn("Not applicable event payload class, skipped! [payloadClass={}]", payloadClass);
        }
        return isApplicable;
    }

    @Override
    public void handle(final ResultSet eventRow, final boolean dryRun) throws Exception {
        final String oldPayloadClass = eventRow.getString(COLUMN_PAYLOAD_CLASS);
        final String newPayloadClass = this.classToClassMapper.get(oldPayloadClass);
        log.info("Migrating event payload class [from={}, to={}]", oldPayloadClass, newPayloadClass);
        if (!dryRun) {
            eventRow.updateString(COLUMN_PAYLOAD_CLASS, newPayloadClass);
        }
    }

}
