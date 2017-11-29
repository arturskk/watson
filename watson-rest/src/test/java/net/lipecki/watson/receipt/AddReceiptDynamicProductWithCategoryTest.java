package net.lipecki.watson.receipt;

import net.lipecki.watson.category.AddCategory;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.product.AddProduct;
import org.junit.Test;

import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class AddReceiptDynamicProductWithCategoryTest extends AddReceiptWithDependenciesBaseTest {

    @Test
    public void shouldUseCategoryForAddedProduct() {
        // given
        final AddReceiptItemDto.AddReceiptItemDtoBuilder item = item();
        item.product(itemProduct(dto -> dto.category(AddReceiptCategoryDto.builder().uuid(CATEGORY_UUID).build())));

        mockProductCreationWithNameAndCategoryForUuid(PRODUCT_NAME, CATEGORY_UUID, PRODUCT_UUID);

        // when
        addReceipt(dto -> dto.item(item.build()));

        // then
        final List<AddReceiptItem> receiptItems = receipt().getItems();
        assertThat(receiptItems)
                .extracting(AddReceiptItem::getProduct)
                .extracting(AddReceiptItemProduct::getCategoryUuid)
                .containsExactly(CATEGORY_UUID);
    }

    @Test
    public void shouldCreateCategoryForAddedProduct() {
        // given
        final AddReceiptItemDto.AddReceiptItemDtoBuilder item = item();
        item.product(itemProduct(dto -> dto.category(AddReceiptCategoryDto.builder().name(CATEGORY_NAME).build())));

        mockCategoryCreationByNameForUuid(CATEGORY_NAME, CATEGORY_UUID);
        mockProductCreationWithNameAndCategoryForUuid(PRODUCT_NAME, CATEGORY_UUID, PRODUCT_UUID);

        // when
        addReceipt(dto -> dto.item(item.build()));

        // then
        final List<AddReceiptItem> receiptItems = receipt().getItems();
        assertThat(receiptItems)
                .extracting(AddReceiptItem::getProduct)
                .extracting(AddReceiptItemProduct::getCategoryUuid)
                .containsExactly(CATEGORY_UUID);
    }

    private AddReceiptProductDto itemProduct(final Consumer<AddReceiptProductDto.AddReceiptProductDtoBuilder> dtoConsumer) {
        final AddReceiptProductDto.AddReceiptProductDtoBuilder itemProduct = AddReceiptProductDto.builder().name(PRODUCT_NAME);
        dtoConsumer.accept(itemProduct);
        return itemProduct.build();
    }

    private AddReceiptItemDto.AddReceiptItemDtoBuilder item() {
        return AddReceiptItemDto.builder()
                .cost(ANY_COST)
                .tags(ANY_TAGS)
                .product(ANY_PRODUCT)
                .amount(ANY_AMOUNT);
    }

    private void mockCategoryCreationByNameForUuid(final String name, final String uuid) {
        when(
                addCategoryCommand.addCategory(AddCategory.builder().type(ReceiptItem.CATEGORY_TYPE).name(name).build())
        ).thenReturn(
                Event.<AddCategory>builder().streamId(uuid).build()
        );
    }

    private void mockProductCreationWithNameAndCategoryForUuid(final String name, final String categoryUuid, final String uuid) {
        final AddProduct expectedAddProduct = AddProduct.builder().name(name).categoryUuid(categoryUuid).build();
        when(
                addProductCommand.addProduct(expectedAddProduct)
        ).thenReturn(
                Event.<AddProduct>builder().streamId(uuid).payload(expectedAddProduct).build()
        );
    }

}
