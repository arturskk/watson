package net.lipecki.watson.receipt;

import net.lipecki.watson.event.Event;
import net.lipecki.watson.product.AddProductData;
import net.lipecki.watson.product.ProductAdded;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class AddSameProductOnceTest extends AddReceiptWithDependenciesBaseTest {

    private static final String PRODUCT_NAME = "sample-product";
    private static final String PRODUCT_UUID_1 = "product-uuid-1";
    private static final String PRODUCT_UUID_2 = "product-uuid-2";

    @Test
    public void shouldAddSameProductOnce() {
        // given
        final AddReceiptProductDto sameProduct = AddReceiptProductDto.builder().name(PRODUCT_NAME).build();
        final List<AddReceiptItemDto> receiptItems = new ArrayList<>();
        receiptItems.add(item(item -> item.product(sameProduct)).build());
        receiptItems.add(item(item -> item.product(sameProduct)).build());

        final AddProductData expectedAddProduct = AddProductData.builder().name(PRODUCT_NAME).categoryUuid(null).build();
        final ProductAdded productAdded = ProductAdded.builder().name(PRODUCT_NAME).categoryUuid(null).build();
        //noinspection unchecked
        when(addProductCommand.addProduct(expectedAddProduct)).thenReturn(
                Event.<ProductAdded>builder().streamId(PRODUCT_UUID_1).payload(productAdded).build(),
                Event.<ProductAdded>builder().streamId(PRODUCT_UUID_2).payload(productAdded).build()
        );

        // when
        addReceipt(dto -> dto.items(receiptItems));

        // then
        assertThat(receipt().getItems())
                .extracting(AddReceiptItemData::getProduct)
                .extracting(AddReceiptItemProduct::getUuid)
                .containsExactly(PRODUCT_UUID_1, PRODUCT_UUID_1);
    }

}
