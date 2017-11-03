package net.lipecki.watson.receipt;

import net.lipecki.watson.WatsonException;
import net.lipecki.watson.WatsonExceptionCode;
import net.lipecki.watson.account.AddAccount;
import net.lipecki.watson.account.AddAccountCommand;
import net.lipecki.watson.category.AddCategory;
import net.lipecki.watson.category.AddCategoryCommand;
import net.lipecki.watson.product.AddProduct;
import net.lipecki.watson.product.AddProductCommand;
import net.lipecki.watson.shop.AddShop;
import net.lipecki.watson.shop.AddShopCommand;
import net.lipecki.watson.store.Event;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class AddReceiptWithDependenciesCommand {

    private final AddReceiptCommand addReceiptCommand;
    private final AddCategoryCommand addCategoryCommand;
    private final AddAccountCommand addAccountCommand;
    private final AddShopCommand addShopCommand;
    private final AddProductCommand addProductCommand;

    public AddReceiptWithDependenciesCommand(
            final AddReceiptCommand addReceiptCommand,
            final AddCategoryCommand addCategoryCommand,
            final AddAccountCommand addAccountCommand,
            final AddShopCommand addShopCommand,
            final AddProductCommand addProductCommand) {
        this.addReceiptCommand = addReceiptCommand;
        this.addCategoryCommand = addCategoryCommand;
        this.addAccountCommand = addAccountCommand;
        this.addShopCommand = addShopCommand;
        this.addProductCommand = addProductCommand;
    }

    public Event<AddReceipt> addReceipt(final AddReceiptDto dto) {
        final String categoryUuid = getCategoryUuid(Receipt.CATEGORY_TYPE, dto.getCategory());
        final String accountUuid = getAccountUuid(dto.getAccount());
        final String shopUuid = getShopUuid(dto.getShop());
        return addReceiptCommand
                .addReceipt(
                        AddReceipt
                                .builder()
                                .tags(new ArrayList<>(dto.getTags()))
                                .date(dto.getDate())
                                .description(dto.getDescription())
                                .categoryUuid(categoryUuid)
                                .accountUuid(accountUuid)
                                .shopUuid(shopUuid)
                                .items(dto.getItems().stream().map(this::asReceiptItem).collect(Collectors.toList()))
                                .build()
                );
    }

    private AddReceiptItem asReceiptItem(final AddReceiptItemDto dto) {
        final String productUuid = getProductUuid(dto.getProduct());
        final String categoryUuid = getCategoryUuid(ReceiptItem.CATEGORY_TYPE, dto.getCategory());
        return AddReceiptItem.builder()
                .cost(dto.getCost())
                .tags(dto.getTags())
                .productUuid(productUuid)
                .categoryUuid(categoryUuid)
                .build();
    }

    private String getCategoryUuid(final String categoryType, final AddReceiptCategoryDto category) {
        if (category.getUuid().isPresent()) {
            return category.getUuid().get();
        } else if (category.getName().isPresent()) {
            return this.addCategoryCommand
                    .addCategory(
                            AddCategory.builder()
                                    .type(categoryType)
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

    private String getProductUuid(final AddReceiptProductDto dto) {
        if (dto.getUuid().isPresent()) {
            return dto.getUuid().get();
        } else if (dto.getName().isPresent()) {
            return this.addProductCommand
                    .addProduct(
                            AddProduct.builder().name(dto.getName().get()).build()
                    )
                    .getAggregateId();
        } else {
            throw new WatsonException(
                    WatsonExceptionCode.BAD_REQUEST,
                    "Neither uuid, nor new account name was present"
            ).with(
                    "dto", dto
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

