package net.lipecki.watson.receipt;

import net.lipecki.watson.event.Event;
import net.lipecki.watson.product.AddProductData;
import net.lipecki.watson.product.ProductAdded;
import org.junit.Test;

import java.util.List;

import static java.util.Collections.singletonList;
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
        addReceipt(
                dto -> dto.items(
                        singletonList(
                                AddReceiptItemDto.builder()
                                        .product(AddReceiptProductDto.builder().uuid(expectedProductUuid).build())
                                        .amount(ANY_AMOUNT)
                                        .cost(expectedCost)
                                        .tags(singletonList(expectedTag))
                                        .build()
                        )
                )
        );

        //then
        final List<AddReceiptItemData> receiptItems = receipt().getItems();
        assertThat(receiptItems)
                .extracting(AddReceiptItemData::getCost)
                .containsExactly(expectedCost);
        assertThat(receiptItems)
                .flatExtracting(AddReceiptItemData::getTags)
                .containsExactly(expectedTag);
        assertThat(receiptItems)
                .extracting(AddReceiptItemData::getProduct)
                .extracting(AddReceiptItemProduct::getUuid)
                .containsExactly(expectedProductUuid);
    }

    @Test
    public void shouldAddProductOnTheFlyIfNeeded() {
        final String expectedProductName = "expectedProductName";
        final String expectedProductUuid = "expectedProductUuid";

        // given
        final AddProductData expectedAddProduct = AddProductData.builder().name(expectedProductName).categoryUuid(null).build();
        final ProductAdded productAdded = ProductAdded.builder().name(expectedProductName).categoryUuid(null).build();
        when(addProductCommand.addProduct(expectedAddProduct))
                .thenReturn(Event.<ProductAdded>builder().streamId(expectedProductUuid).payload(productAdded).build());

        // when
        addReceipt(dto -> dto.items(
                singletonList(
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
        final List<AddReceiptItemData> receiptItems = receipt().getItems();
        assertThat(receiptItems)
                .extracting(AddReceiptItemData::getProduct)
                .extracting(AddReceiptItemProduct::getUuid)
                .containsExactly(expectedProductUuid);
    }

}
