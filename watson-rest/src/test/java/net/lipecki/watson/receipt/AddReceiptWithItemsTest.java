package net.lipecki.watson.receipt;

import net.lipecki.watson.event.Event;
import net.lipecki.watson.product.AddProduct;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


public class AddReceiptWithItemsTest extends AddReceiptWithDependenciesBaseTest {

    private static final AddReceiptAmountDto ANY_AMOUNT = AddReceiptAmountDto.builder().build();

    @Test
    public void shouldAddItemWithData() {
        // given
        final String expectedTag = "any-tag";
        final String expectedCost = "any-cost";
        final String expectedProductUuid = "expectedProductUuid";

        // when
        addReceipt(dto -> dto.items(
                Arrays.asList(
                        AddReceiptItemDto.builder()
                                .product(AddReceiptProductDto.builder().uuid(expectedProductUuid).build())
                                .amount(ANY_AMOUNT)
                                .cost(expectedCost)
                                .tags(Arrays.asList(expectedTag))
                                .build()
                )
                )
        );

        //then
        final List<AddReceiptItem> receiptItems = receipt().getItems();
        assertThat(receiptItems).extracting(AddReceiptItem::getCost).containsExactly(expectedCost);
        assertThat(receiptItems).flatExtracting(AddReceiptItem::getTags).containsExactly(expectedTag);
        assertThat(receiptItems).extracting(AddReceiptItem::getProductUuid).containsExactly(expectedProductUuid);
    }

    @Test
    public void shouldAddProductOnTheFlyIfNeeded() {
        final String expectedProductName = "expectedProductName";
        final String expectedProductUuid = "expectedProductUuid";

        // given
        when(
                addProductCommand.addProduct(
                        AddProduct.builder()
                                .name(expectedProductName)
                                .build()
                )
        ).thenReturn(
                Event.<AddProduct>builder().streamId(expectedProductUuid).build()
        );

        // when
        addReceipt(dto -> dto.items(
                Arrays.asList(
                        AddReceiptItemDto.builder()
                                .product(
                                        AddReceiptProductDto.builder()
                                                .name(expectedProductName)
                                                .build()
                                )
                                .amount(ANY_AMOUNT)
                                .build()
                )
        ));

        // then
        final List<AddReceiptItem> receiptItems = receipt().getItems();
        assertThat(receiptItems).extracting(AddReceiptItem::getProductUuid).containsExactly(expectedProductUuid);
    }

}
