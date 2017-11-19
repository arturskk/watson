package net.lipecki.watson.receipt;

import net.lipecki.watson.category.AddCategory;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.product.AddProduct;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


public class AddReceiptWithItemsTest extends AddReceiptWithDependenciesBaseTest {

    private static final AddReceiptProductDto ANY_PRODUCT = AddReceiptProductDto.builder().uuid(UUID.randomUUID().toString()).build();
    private static final AddReceiptCategoryDto ANY_CATEGORY = AddReceiptCategoryDto.builder().uuid(UUID.randomUUID().toString()).build();
    private static final AddReceiptAmountDto ANY_AMOUNT = AddReceiptAmountDto.builder().build();

    @Test
    public void shouldAddItemWithData() {
        // given
        final String expectedTag = "any-tag";
        final String expectedCost = "any-cost";
        final String expectedCategoryUuid = "expectedCategoryUuid";
        final String expectedProductUuid = "expectedProductUuid";

        // when
        addReceipt(dto -> dto.items(
                Arrays.asList(
                        AddReceiptItemDto.builder()
                                .product(
                                        AddReceiptProductDto.builder().uuid(expectedProductUuid).build()
                                )
                                .category(
                                        AddReceiptCategoryDto.builder().uuid(expectedCategoryUuid).build()
                                )
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
        assertThat(receiptItems).extracting(AddReceiptItem::getCategoryUuid).containsExactly(expectedCategoryUuid);
        assertThat(receiptItems).extracting(AddReceiptItem::getProductUuid).containsExactly(expectedProductUuid);
    }

    @Test
    public void shouldAddCategoryOnTheFlyIfNeeded() {
        final String expectedCategoryName = "expectedCategoryName";
        final String expectedCategoryUuid = "expectedCategoryUuid";

        // given
        when(
                addCategoryCommand.addCategory(
                        AddCategory.builder()
                                .type(ReceiptItem.CATEGORY_TYPE)
                                .name(expectedCategoryName)
                                .build()
                )
        ).thenReturn(
                Event.<AddCategory>builder().streamId(expectedCategoryUuid).build()
        );

        // when
        addReceipt(dto -> dto.items(
                Arrays.asList(
                        AddReceiptItemDto.builder()
                                .product(ANY_PRODUCT)
                                .category(
                                        AddReceiptCategoryDto.builder()
                                                .name(expectedCategoryName)
                                                .build()
                                )
                                .amount(ANY_AMOUNT)
                                .build()
                )
        ));

        // then
        final List<AddReceiptItem> receiptItems = receipt().getItems();
        assertThat(receiptItems).extracting(AddReceiptItem::getCategoryUuid).containsExactly(expectedCategoryUuid);
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
                                .category(ANY_CATEGORY)
                                .build()
                )
        ));

        // then
        final List<AddReceiptItem> receiptItems = receipt().getItems();
        assertThat(receiptItems).extracting(AddReceiptItem::getProductUuid).containsExactly(expectedProductUuid);
    }

}
