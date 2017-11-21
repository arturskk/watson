package net.lipecki.watson.receipt;

import net.lipecki.watson.event.Event;
import net.lipecki.watson.product.AddProduct;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class AddSameProductOnceTest extends AddReceiptWithDependenciesBaseTest {

    public static final String PRODUCT_NAME = "sample-product";
    public static final String PRODUCT_UUID_1 = "product-uuid-1";
    public static final String PRODUCT_UUID_2 = "product-uuid-2";
    public static final AddReceiptAmountDto ANY_AMOUNT  = AddReceiptAmountDto.builder().build();

    @Test
    public void shouldAddSameProductOnce() {
        // given
        final AddReceiptProductDto sameProduct = AddReceiptProductDto.builder().name(PRODUCT_NAME).build();
        final List<AddReceiptItemDto> receiptItems = new ArrayList<>();
        receiptItems.add(item(item -> item.product(sameProduct)));
        receiptItems.add(item(item -> item.product(sameProduct)));

        when(
                addProductCommand.addProduct(
                        eq(AddProduct.builder().name(PRODUCT_NAME).build())
                )
        ).thenReturn(
                Event.<AddProduct>builder().streamId(PRODUCT_UUID_1).build(),
                Event.<AddProduct>builder().streamId(PRODUCT_UUID_2).build()
        );

        // when
        addReceipt(dto -> dto.items(receiptItems));

        // then
        assertThat(receipt().getItems()).extracting("productUuid").containsExactly(PRODUCT_UUID_1, PRODUCT_UUID_1);
    }

    private AddReceiptItemDto item(final Consumer<AddReceiptItemDto.AddReceiptItemDtoBuilder> consumer) {
        final AddReceiptItemDto.AddReceiptItemDtoBuilder dto = AddReceiptItemDto.builder().amount(ANY_AMOUNT);
        consumer.accept(dto);
        return dto.build();
    }

}
