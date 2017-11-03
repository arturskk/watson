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
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
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
                                .tags(new ArrayList<>(dto.getTags()))
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
                .categoryUuid(getCategoryUuid(ReceiptItem.CATEGORY_TYPE, dto.getCategory()))
                .build();
    }

    private String getCategoryUuid(final String categoryType, final AddReceiptCategoryDto addReceiptCategoryDto) {
        return getOrCreateUuid(
                addReceiptCategoryDto,
                AddReceiptCategoryDto::getUuid,
                dto -> dto.getName()
                        .map(name -> AddCategory.builder().type(categoryType).name(name).build())
                        .map(addCategoryCommand::addCategory)
                        .map(Event::getAggregateId)
                        .orElseThrow(throwMissingDataException(dto))
        );
    }

    private String getProductUuid(final AddReceiptProductDto addReceiptProductDto) {
        return getOrCreateUuid(
                addReceiptProductDto,
                AddReceiptProductDto::getUuid,
                dto -> dto.getName()
                        .map(name -> AddProduct.builder().name(name).build())
                        .map(addProductCommand::addProduct)
                        .map(Event::getAggregateId)
                        .orElseThrow(throwMissingDataException(dto))
        );
    }

    private String getAccountUuid(final AddReceiptAccountDto addReceiptAccountDto) {
        return getOrCreateUuid(
                addReceiptAccountDto,
                AddReceiptAccountDto::getUuid,
                dto -> dto.getName()
                        .map(name -> AddAccount.builder().name(name).build())
                        .map(addAccountCommand::addAccount)
                        .map(Event::getAggregateId)
                        .orElseThrow(throwMissingDataException(dto))
        );
    }

    private String getShopUuid(final AddReceiptShopDto addReceiptShopDto) {
        return getOrCreateUuid(
                addReceiptShopDto,
                AddReceiptShopDto::getUuid,
                dto -> dto.getName()
                        .map(name -> AddShop.builder().name(name).build())
                        .map(addShopCommand::addShop)
                        .map(Event::getAggregateId)
                        .orElseThrow(throwMissingDataException(dto))
        );
    }

    private <T> String getOrCreateUuid(
            final T dto,
            final Function<T, Optional<String>> uuidExtractor,
            final Function<T, String> uuidGenerator) {
        return uuidExtractor.apply(dto).orElseGet(() -> uuidGenerator.apply(dto));
    }

    private <T> Supplier<WatsonException> throwMissingDataException(final T dto) {
        return () -> new WatsonException(WatsonExceptionCode.BAD_REQUEST, "Missing data to create object")
                .with("type", dto.getClass().getSimpleName())
                .with("dto", dto);
    }

}

