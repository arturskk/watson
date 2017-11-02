package net.lipecki.watson.receipt;

import net.lipecki.watson.WatsonException;
import net.lipecki.watson.WatsonExceptionCode;
import net.lipecki.watson.account.AddAccount;
import net.lipecki.watson.account.AddAccountCommand;
import net.lipecki.watson.category.AddCategory;
import net.lipecki.watson.category.AddCategoryCommand;
import net.lipecki.watson.shop.AddShop;
import net.lipecki.watson.shop.AddShopCommand;
import net.lipecki.watson.store.Event;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AddReceiptWithDependenciesCommand {

    private final AddReceiptCommand addReceiptCommand;
    private final AddCategoryCommand addCategoryCommand;
    private final AddAccountCommand addAccountCommand;
    private final AddShopCommand addShopCommand;

    public AddReceiptWithDependenciesCommand(
            final AddReceiptCommand addReceiptCommand,
            final AddCategoryCommand addCategoryCommand,
            final AddAccountCommand addAccountCommand,
            final AddShopCommand addShopCommand) {
        this.addReceiptCommand = addReceiptCommand;
        this.addCategoryCommand = addCategoryCommand;
        this.addAccountCommand = addAccountCommand;
        this.addShopCommand = addShopCommand;
    }

    public Event<AddReceipt> addReceipt(final AddReceiptDto addReceipt) {
        final String categoryUuid = getCategoryUuid(addReceipt.getCategory());
        final String accountUuid = getAccountUuid(addReceipt.getAccount());
        final String shopUuid = getShopUuid(addReceipt.getShop());
        return addReceiptCommand
                .addReceipt(
                        AddReceipt
                                .builder()
                                .tags(new ArrayList<>(addReceipt.getTags()))
                                .date(addReceipt.getDate())
                                .description(addReceipt.getDescription())
                                .categoryUuid(categoryUuid)
                                .accountUuid(accountUuid)
                                .shopUuid(shopUuid)
                                .build()
                );
    }

    private String getCategoryUuid(final AddReceiptCategoryDto category) {
        if (category.getUuid().isPresent()) {
            return category.getUuid().get();
        } else if (category.getName().isPresent()) {
            return this.addCategoryCommand
                    .addCategory(
                            AddCategory.builder()
                                    .type(Receipt.CATEGORY_TYPE)
                                    .name(category.getName().get())
                                    .build()
                    )
                    .getAggregateId();
        } else {
            throw new WatsonException(
                    WatsonExceptionCode.BAD_REQUEST,
                    "Neither uuid, nor new category name was present"
            ).with(
                    "category", category
            );
        }
    }

    private String getAccountUuid(final AddReceiptAccountDto account) {
        if (account.getUuid().isPresent()) {
            return account.getUuid().get();
        } else if (account.getName().isPresent()) {
            return this.addAccountCommand
                    .addAccount(
                            AddAccount.builder().name(account.getName().get()).build()
                    )
                    .getAggregateId();
        } else {
            throw new WatsonException(
                    WatsonExceptionCode.BAD_REQUEST,
                    "Neither uuid, nor new account name was present"
            ).with(
                    "account", account
            );
        }
    }

    private String getShopUuid(final AddReceiptShopDto shop) {
        if (shop.getUuid().isPresent()) {
            return shop.getUuid().get();
        } else if (shop.getName().isPresent()) {
            return this.addShopCommand
                    .addShop(
                            AddShop.builder().name(shop.getName().get()).build()
                    )
                    .getAggregateId();
        } else {
            throw new WatsonException(
                    WatsonExceptionCode.BAD_REQUEST,
                    "Neither uuid, nor new shop name was present"
            ).with(
                    "shop", shop
            );
        }
    }

}

