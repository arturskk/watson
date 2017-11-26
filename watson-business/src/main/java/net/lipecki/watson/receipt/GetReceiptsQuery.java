package net.lipecki.watson.receipt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GetReceiptsQuery {

    private final ReceiptStore receiptStore;

    public GetReceiptsQuery(final ReceiptStore receiptStore) {
        this.receiptStore = receiptStore;
    }

    public List<Receipt> getReceipts() {
        return this.receiptStore.getReceipts();
    }

}
