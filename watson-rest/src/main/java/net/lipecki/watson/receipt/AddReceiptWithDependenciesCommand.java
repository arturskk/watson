package net.lipecki.watson.receipt;

import net.lipecki.watson.WatsonException;
import net.lipecki.watson.WatsonExceptionCode;
import net.lipecki.watson.account.AddAccount;
import net.lipecki.watson.account.AddAccountCommand;
import net.lipecki.watson.category.AddCategory;
import net.lipecki.watson.category.AddCategoryCommand;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.product.AddProduct;
import net.lipecki.watson.product.AddProductCommand;
import net.lipecki.watson.shop.AddShop;
import net.lipecki.watson.shop.AddShopCommand;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;
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
        return addReceiptCommand
                .addReceipt(
                        AddReceipt
                                .builder()
                                .tags(dto.getTags())
                                .date(dto.getDate())
                                .description(dto.getDescription())
                                .categoryUuid(getCategoryUuid(Receipt.CATEGORY_TYPE, dto.getCategory()))
                                .accountUuid(getAccountUuid(dto.getAccount()))
                                .shopUuid(getShopUuid(dto.getShop()))
                                .items(dto.getItems().stream().map(this::asReceiptItem).collect(Collectors.toList()))
                                .build()
                );
    }

    private AddReceiptItem asReceiptItem(final AddReceiptItemDto dto) {
        return AddReceiptItem.builder()
                .cost(dto.getCost())
                .tags(dto.getTags())
                .productUuid(getProductUuid(dto.getProduct()))
                .amount(
                        AddReceiptItemAmount.builder()
                                .count(dto.getAmount().getCount())
                                .unit(dto.getAmount().getUnit())
                                .build()
                )
                .build();
    }

    private String getCategoryUuid(final String categoryType, final AddReceiptCategoryDto dto) {
        return getUuidOrCreateObject(
                dto,
                AddReceiptCategoryDto::getUuid,
                asAddCategory(categoryType),
                addCategoryCommand::addCategory
        );
    }

    private Function<AddReceiptCategoryDto, Optional<AddCategory>> asAddCategory(final String categoryType) {
        return dto -> dto.getName().map(name -> AddCategory.builder().type(categoryType).name(name).build());
    }

    private String getProductUuid(final AddReceiptProductDto dto) {
        return getUuidOrCreateObject(
                dto,
                AddReceiptProductDto::getUuid,
                this::asAddProduct,
                addProductCommand::addProduct
        );
    }

    private Optional<AddProduct> asAddProduct(final AddReceiptProductDto dto) {
        return dto.getName().map(name -> AddProduct.builder().name(name).build());
    }

    private String getAccountUuid(final AddReceiptAccountDto dto) {
        return getUuidOrCreateObject(
                dto,
                AddReceiptAccountDto::getUuid,
                this::asAddAccount,
                addAccountCommand::addAccount
        );
    }

    private Optional<AddAccount> asAddAccount(final AddReceiptAccountDto dto) {
        return dto.getName().map(name -> AddAccount.builder().name(name).build());
    }

    private String getShopUuid(final AddReceiptShopDto dto) {
        return getUuidOrCreateObject(
                dto,
                AddReceiptShopDto::getUuid,
                this::asAddShop,
                addShopCommand::addShop
        );
    }

    private Optional<AddShop> asAddShop(final AddReceiptShopDto dto) {
        return dto.getName().map(name -> AddShop.builder().name(name).build());
    }

    private <T, M> String getUuidOrCreateObject(
            final T dto,
            final Function<T, Optional<String>> uuidExtractor,
            final Function<T, Optional<M>> objectMapper,
            final Function<M, Event<M>> uuidGenerator) {
        final Optional<String> existingUuid = uuidExtractor.apply(dto);
        if (existingUuid.isPresent()) {
            return existingUuid.get();
        }

        final M creationModel = objectMapper.apply(dto).orElseThrow(
                () -> WatsonException.of(WatsonExceptionCode.UNKNOWN, "Can't create receipt dependency on the fly")
                        .with("type", dto.getClass().getSimpleName())
                        .with("dto", dto)
        );

        return uuidGenerator.apply(creationModel).getStreamId();
    }

}

