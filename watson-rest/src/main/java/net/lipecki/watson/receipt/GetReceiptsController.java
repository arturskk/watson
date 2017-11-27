package net.lipecki.watson.receipt;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.rest.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(Api.V1)
public class GetReceiptsController {

    private static final DateTimeFormatter fullDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final GetReceiptsQuery query;

    public GetReceiptsController(final GetReceiptsQuery query) {
        this.query = query;
    }

    @GetMapping("/receipt")
    public List<ReceiptSummaryDto> getAllReceipts() {
        return this.query
                .getReceipts()
                .stream()
                .map(this::asReceiptDto)
                .collect(Collectors.toList());
    }

    private ReceiptSummaryDto asReceiptDto(final Receipt receipt) {
        return ReceiptSummaryDto
                .builder()
                .uuid(receipt.getUuid())
                .cost(receipt.getCost().getDescription())
                .date(receipt.getDate().format(fullDateFormat))
                .accountName(receipt.getAccount().getName())
                .categoryName(receipt.getCategory().getName())
                .categoryPath(receipt.getCategory().getCategoryPath())
                .shopName(receipt.getShop().getName())
                .items(receipt.getItems().stream().map(this::asReceiptItemDto).collect(Collectors.toList()))
                .build();
    }

    private ReceiptSummaryDto.ReceiptSummaryItem asReceiptItemDto(final ReceiptItem receiptItem) {
        return ReceiptSummaryDto
                .ReceiptSummaryItem
                .builder()
                .cost(receiptItem.getCost().getDescription())
                .amount(receiptItem.getAmount().getCount())
                .unit(receiptItem.getAmount().getUnit().getName())
                .productName(receiptItem.getProduct().getName())
                .categoryName(receiptItem.getCategory().getName())
                .categoryPath(receiptItem.getCategory().getCategoryPath())
                .build();
    }

}
