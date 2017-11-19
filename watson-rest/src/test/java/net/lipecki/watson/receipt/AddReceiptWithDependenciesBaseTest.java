package net.lipecki.watson.receipt;

import net.lipecki.watson.account.AddAccountCommand;
import net.lipecki.watson.category.AddCategoryCommand;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.product.AddProductCommand;
import net.lipecki.watson.shop.AddShopCommand;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.function.Consumer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class AddReceiptWithDependenciesBaseTest {

    protected static final String DEFAULT_UUID = "default-uuid";
    protected static final String RECEIPT_UUID = "receipt-0000-0000-0000-000000000001";
    protected static final String CATEGORY_NAME = "sample-category";
    protected static final String CATEGORY_UUID = "category-0000-0000-0000-000000000001";
    protected static final String ACCOUNT_NAME = "account-category";
    protected static final String ACCOUNT_UUID = "account-0000-0000-0000-000000000001";
    protected static final String SHOP_NAME = "shop-category";
    protected static final String SHOP_UUID = "shop-0000-0000-0000-000000000001";

    protected AddReceiptCommand addReceiptCommand;
    protected AddShopCommand addShopCommand;
    protected AddAccountCommand addAccountCommand;
    protected AddCategoryCommand addCategoryCommand;
    protected AddProductCommand addProductCommand;

    protected AddReceiptWithDependenciesCommand uut;
    protected ArgumentCaptor<AddReceipt> addReceiptCaptor;

    @Before
    public void setUp() {
        this.addReceiptCommand = mock(AddReceiptCommand.class);
        this.addShopCommand = mock(AddShopCommand.class);
        this.addAccountCommand = mock(AddAccountCommand.class);
        this.addCategoryCommand = mock(AddCategoryCommand.class);
        this.addProductCommand = mock(AddProductCommand.class);
        this.addReceiptCaptor = ArgumentCaptor.forClass(AddReceipt.class);
        when(
                this.addReceiptCommand.addReceipt(this.addReceiptCaptor.capture())
        ).thenReturn(
                Event.<AddReceipt> builder().streamId(RECEIPT_UUID).build()
        );

        this.uut = new AddReceiptWithDependenciesCommand(
                this.addReceiptCommand,
                this.addCategoryCommand,
                this.addAccountCommand,
                this.addShopCommand,
                this.addProductCommand
        );
    }

    /**
     * Wraps add receipt action with lambda ready consumer and default values propagation.
     * <p>
     * Provides default non null values for all required dto fields and allows consumer to override them.
     *
     * @param consumer
     * @return
     */
    protected Event<AddReceipt> addReceipt(final Consumer<AddReceiptDto.AddReceiptDtoBuilder> consumer) {
        final AddReceiptDto.AddReceiptDtoBuilder dtoBuilder = AddReceiptDto.builder();

        dtoBuilder.description(StringUtils.EMPTY);
        dtoBuilder.date("2000-01-01");
        dtoBuilder.tags(new ArrayList<>());
        dtoBuilder.shop(AddReceiptShopDto.builder().uuid(DEFAULT_UUID).build());
        dtoBuilder.account(AddReceiptAccountDto.builder().uuid(DEFAULT_UUID).build());
        dtoBuilder.category(AddReceiptCategoryDto.builder().uuid(DEFAULT_UUID).build());

        consumer.accept(dtoBuilder);
        return uut.addReceipt(dtoBuilder.build());
    }

    protected AddReceipt receipt() {
        return addReceiptCaptor.getValue();
    }

}
