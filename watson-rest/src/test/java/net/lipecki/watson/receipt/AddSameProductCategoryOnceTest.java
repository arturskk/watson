package net.lipecki.watson.receipt;

import net.lipecki.watson.category.AddCategory;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.product.AddProduct;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AddSameProductCategoryOnceTest extends AddReceiptWithDependenciesBaseTest {

    private static final String CATEGORY_NAME = "sample-category";
    private static final String CATEGORY_UUID_1 = "category-uuid-1";
    private static final String CATEGORY_UUID_2 = "category-uuid-2";
    private static final AddReceiptAmountDto ANY_AMOUNT = AddReceiptAmountDto.builder().build();

    @Test
    public void shouldAddProductCategoryOnce() {
        // given
        final AddReceiptCategoryDto addCategoryDto = AddReceiptCategoryDto.builder().name(CATEGORY_NAME).build();
        final List<AddReceiptItemDto> receiptItems = new ArrayList<>();
        receiptItems.add(item(item -> item.product(AddReceiptProductDto.builder().category(addCategoryDto).name(PRODUCT_NAME + "_1").build())));
        receiptItems.add(item(item -> item.product(AddReceiptProductDto.builder().category(addCategoryDto).name(PRODUCT_NAME + "_2").build())));

        when(addProductCommand.addProduct(any(AddProduct.class))).thenReturn(
                Event.<AddProduct>builder().streamId(PRODUCT_UUID).payload(AddProduct.builder().name(PRODUCT_NAME).categoryUuid(CATEGORY_UUID_1).build()).build()
        );

        final AddCategory expectedAddCategory = AddCategory.builder().type(ReceiptItem.CATEGORY_TYPE).name(CATEGORY_NAME).build();
        //noinspection unchecked
        when(addCategoryCommand.addCategory(expectedAddCategory)).thenReturn(
                Event.<AddCategory>builder().streamId(CATEGORY_UUID_1).payload(expectedAddCategory).build(),
                Event.<AddCategory>builder().streamId(CATEGORY_UUID_2).payload(expectedAddCategory).build()
        );

        // when
        addReceipt(dto -> dto.items(receiptItems));

        // then
        assertThat(receipt().getItems())
                .extracting(AddReceiptItem::getProduct)
                .extracting(AddReceiptItemProduct::getCategoryUuid)
                .containsExactly(CATEGORY_UUID_1, CATEGORY_UUID_1);
    }

    private AddReceiptItemDto item(final Consumer<AddReceiptItemDto.AddReceiptItemDtoBuilder> consumer) {
        final AddReceiptItemDto.AddReceiptItemDtoBuilder dto = AddReceiptItemDto.builder().amount(ANY_AMOUNT);
        consumer.accept(dto);
        return dto.build();
    }

}
