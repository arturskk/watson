package net.lipecki.watson.receipt;

import net.lipecki.watson.category.AddCategoryData;
import net.lipecki.watson.category.CategoryAdded;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.product.AddProductData;
import net.lipecki.watson.product.ProductAdded;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class AddReceiptDynamicProductWithCategoryTest extends AddReceiptWithDependenciesBaseTest {

    @Test
    public void shouldUseCategoryForAddedProduct() {
        // given
        final AddReceiptItemDto.AddReceiptItemDtoBuilder item = item();
        item.product(product(dto -> {
            dto.name(PRODUCT_NAME);
            dto.category(AddReceiptCategoryDto.builder().uuid(CATEGORY_UUID).build());
        }));

        mockProductCreationWithNameAndCategoryForUuid(PRODUCT_NAME, CATEGORY_UUID, PRODUCT_UUID);

        // when
        addReceipt(dto -> dto.item(item.build()));

        // then
        final List<AddReceiptItemData> receiptItems = receipt().getItems();
        assertThat(receiptItems)
                .extracting(AddReceiptItemData::getProduct)
                .extracting(AddReceiptItemProduct::getCategoryUuid)
                .containsExactly(CATEGORY_UUID);
    }

    @Test
    public void shouldCreateCategoryForAddedProduct() {
        // given
        final AddReceiptItemDto.AddReceiptItemDtoBuilder item = item();
        item.product(product(dto -> {
            dto.name(PRODUCT_NAME);
            dto.category(AddReceiptCategoryDto.builder().name(CATEGORY_NAME).build());
        }));

        mockCategoryCreationByNameForUuid(CATEGORY_NAME, CATEGORY_UUID);
        mockProductCreationWithNameAndCategoryForUuid(PRODUCT_NAME, CATEGORY_UUID, PRODUCT_UUID);

        // when
        addReceipt(dto -> dto.item(item.build()));

        // then
        final List<AddReceiptItemData> receiptItems = receipt().getItems();
        assertThat(receiptItems)
                .extracting(AddReceiptItemData::getProduct)
                .extracting(AddReceiptItemProduct::getCategoryUuid)
                .containsExactly(CATEGORY_UUID);
    }

    private void mockCategoryCreationByNameForUuid(final String name, final String uuid) {
        when(
                addCategoryCommand.addCategory(AddCategoryData.builder().type(ReceiptItem.CATEGORY_TYPE).name(name).build())
        ).thenReturn(
                Event.<CategoryAdded>builder().streamId(uuid).build()
        );
    }

    private void mockProductCreationWithNameAndCategoryForUuid(final String name, final String categoryUuid, final String uuid) {
        final AddProductData expectedAddProduct = AddProductData.builder().name(name).categoryUuid(categoryUuid).build();
        final ProductAdded productAdded = ProductAdded.builder().name(name).categoryUuid(categoryUuid).build();
        when(
                addProductCommand.addProduct(expectedAddProduct)
        ).thenReturn(
                Event.<ProductAdded>builder().streamId(uuid).payload(productAdded).build()
        );
    }

}
