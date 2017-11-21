package net.lipecki.watson.receipt;

import net.lipecki.watson.WatsonException;
import net.lipecki.watson.account.AddAccount;
import net.lipecki.watson.category.AddCategory;
import net.lipecki.watson.shop.AddShop;
import net.lipecki.watson.event.Event;
import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


public class AddReceiptWithOptionalDataTest extends AddReceiptWithDependenciesBaseTest {

    @Test
    public void shouldAddReceiptWithProvidedData() {
        // given
        final String expectedDescription = "expected-description";
        final String expectedDate = "2017-01-01";
        final String expectedTag1 = "tag1";
        final String expectedTag2 = "tag2";

        // when
        addReceipt(
                dto -> dto
                        .description(expectedDescription)
                        .date(expectedDate)
                        .tags(Arrays.asList(expectedTag1, expectedTag2))
        );

        // then
        assertThat(receipt().getDate()).isEqualTo(expectedDate);
        assertThat(receipt().getTags()).containsExactly(expectedTag1, expectedTag2);
        assertThat(receipt().getDescription()).isEqualTo(expectedDescription);
    }

    @Test
    public void shouldAddReceiptCategoryOnTheFly() {
        // given
        when(
                addCategoryCommand.addCategory(
                        AddCategory.builder()
                                .type(Receipt.CATEGORY_TYPE)
                                .name(CATEGORY_NAME)
                                .build()
                )
        ).thenReturn(
                Event.<AddCategory> builder().streamId(CATEGORY_UUID).build()
        );

        // when
        addReceipt(dto -> dto.category(AddReceiptCategoryDto.builder().name(CATEGORY_NAME).build()));

        // then
        assertThat(receipt().getCategoryUuid()).isEqualTo(CATEGORY_UUID);
    }

    @Test
    public void shouldUseExistingCategoryIfProvided() {
        // when
        addReceipt(dto -> dto.category(AddReceiptCategoryDto.builder().uuid(CATEGORY_UUID).build()));

        // then
        assertThat(receipt().getCategoryUuid()).isEqualTo(CATEGORY_UUID);
    }

    @Test(expected = WatsonException.class)
    public void shouldThrowExceptionOnMissingCategoryDto() {
        addReceipt(dto -> dto.category(AddReceiptCategoryDto.builder().build()));
    }

    @Test
    public void shouldAddReceiptAccountOnTheFly() {
        // given
        when(
                addAccountCommand.addAccount(AddAccount.builder().name(ACCOUNT_NAME).build())
        ).thenReturn(
                Event.<AddAccount> builder().streamId(ACCOUNT_UUID).build()
        );

        // when
        addReceipt(dto -> dto.account(AddReceiptAccountDto.builder().name(ACCOUNT_NAME).build()));

        // then
        assertThat(receipt().getAccountUuid()).isEqualTo(ACCOUNT_UUID);
    }

    @Test
    public void shouldUseExistingAccountIfProvided() {
        // when
        addReceipt(dto -> dto.account(AddReceiptAccountDto.builder().uuid(ACCOUNT_UUID).build()));

        // then
        assertThat(receipt().getAccountUuid()).isEqualTo(ACCOUNT_UUID);
    }

    @Test(expected = WatsonException.class)
    public void shouldThrowExceptionOnMissingAccountDto() {
        addReceipt(dto -> dto.account(AddReceiptAccountDto.builder().build()));
    }

    @Test
    public void shouldAddReceiptShopOnTheFly() {
        // given
        when(
                addShopCommand.addShop(AddShop.builder().name(SHOP_NAME).build())
        ).thenReturn(
                Event.<AddShop> builder().streamId(SHOP_UUID).build()
        );

        // when
        addReceipt(dto -> dto.shop(AddReceiptShopDto.builder().name(SHOP_NAME).build()));

        // then
        assertThat(receipt().getShopUuid()).isEqualTo(SHOP_UUID);
    }

    @Test
    public void shouldUseExistingShopIfProvided() {
        // when
        addReceipt(dto -> dto.shop(AddReceiptShopDto.builder().uuid(SHOP_UUID).build()));

        // then
        assertThat(receipt().getShopUuid()).isEqualTo(SHOP_UUID);
    }

    @Test(expected = WatsonException.class)
    public void shouldThrowExceptionOnMissingShopDto() {
        addReceipt(dto -> dto.shop(AddReceiptShopDto.builder().build()));
    }

}
