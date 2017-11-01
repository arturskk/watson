package net.lipecki.watson.receipt;

import net.lipecki.watson.WatsonException;
import net.lipecki.watson.account.AddAccountService;
import net.lipecki.watson.category.AddCategoryService;
import net.lipecki.watson.shop.AddShopService;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AddReceiptWithOptionalDataTest {

    private static final String DEFAULT_UUID = "default-uuid";
    private static final String RECEIPT_UUID = "receipt-0000-0000-0000-000000000001";
    private static final String CATEGORY_NAME = "sample-category";
    private static final String CATEGORY_UUID = "category-0000-0000-0000-000000000001";
    private static final String ACCOUT_NAME = "account-category";
    private static final String ACCOUNT_UUID = "account-0000-0000-0000-000000000001";
    private static final String SHOP_NAME = "shop-category";
    private static final String SHOP_UUID = "shop-0000-0000-0000-000000000001";

    @Mock
    private AddReceiptService addReceiptService;
    @Mock
    private AddShopService addShopService;
    @Mock
    private AddAccountService addAccountService;
    @Mock
    private AddCategoryService addCategoryService;
    @InjectMocks
    private AddReceiptWithDependenciesService uut;
    private ArgumentCaptor<AddReceipt> addReceiptCaptor;

    @Before
    public void setUpMocks() {
        addReceiptCaptor = ArgumentCaptor.forClass(AddReceipt.class);
        when(addReceiptService.addReceipt(addReceiptCaptor.capture())).thenReturn(RECEIPT_UUID);
    }

    @Test
    public void shouldAddReceiptWithProvidedData() {
        // given
        final String expectedBudgetId = "budget-id";
        final String expectedDate = "2017-01-01";
        final String expectedTag1 = "tag1";
        final String expectedTag2 = "tag2";

        // when
        addReceipt(
                dto -> dto
                        .budgetUuid(expectedBudgetId)
                        .date(expectedDate)
                        .tags(Arrays.asList(expectedTag1, expectedTag2))
        );

        // then
        assertThat(receipt().getBudgetUuid()).isEqualTo(expectedBudgetId);
        assertThat(receipt().getDate()).isEqualTo(LocalDate.of(2017, 1, 1));
        assertThat(receipt().getTags()).containsExactly(expectedTag1, expectedTag2);
    }

    @Test
    public void shouldAddReceiptCategoryOnTheFly() {
        // given
        when(addCategoryService.addCategory(Receipt.CATEGORY_TYPE, CATEGORY_NAME)).thenReturn(CATEGORY_UUID);

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
        when(addAccountService.addAccount(ACCOUT_NAME)).thenReturn(ACCOUNT_UUID);

        // when
        addReceipt(dto -> dto.account(AddReceiptAccountDto.builder().name(ACCOUT_NAME).build()));

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
        when(addShopService.addShop(SHOP_NAME)).thenReturn(SHOP_UUID);

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

    /**
     * Wraps add receipt action with lambda ready consumer and default values propagation.
     * <p>
     * Provides default non null values for all required dto fields and allows consumer to override them.
     *
     * @param consumer
     * @return
     */
    private String addReceipt(final Consumer<AddReceiptDto.AddReceiptDtoBuilder> consumer) {
        final AddReceiptDto.AddReceiptDtoBuilder dtoBuilder = AddReceiptDto.builder();

        dtoBuilder.date("2000-01-01");
        dtoBuilder.budgetUuid(StringUtils.EMPTY);
        dtoBuilder.tags(new ArrayList<>());
        dtoBuilder.shop(AddReceiptShopDto.builder().uuid(DEFAULT_UUID).build());
        dtoBuilder.account(AddReceiptAccountDto.builder().uuid(DEFAULT_UUID).build());
        dtoBuilder.category(AddReceiptCategoryDto.builder().uuid(DEFAULT_UUID).build());

        consumer.accept(dtoBuilder);
        return uut.addReceipt(dtoBuilder.build());
    }

    private AddReceipt receipt() {
        return addReceiptCaptor.getValue();
    }

}
