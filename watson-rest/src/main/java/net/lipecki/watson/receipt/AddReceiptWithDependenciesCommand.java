package net.lipecki.watson.receipt;

import net.lipecki.watson.WatsonException;
import net.lipecki.watson.WatsonExceptionCode;
import net.lipecki.watson.account.AddAccountCommand;
import net.lipecki.watson.account.AddAccountData;
import net.lipecki.watson.category.AddCategoryCommand;
import net.lipecki.watson.category.AddCategoryData;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.product.AddProductCommand;
import net.lipecki.watson.product.AddProductData;
import net.lipecki.watson.shop.AddShopCommand;
import net.lipecki.watson.shop.AddShopData;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public Event addReceipt(final AddReceiptDto dto) {
        return addReceiptCommand
                .addReceipt(
                        AddReceiptData
                                .builder()
                                .tags(dto.getTags())
                                .date(dto.getDate())
                                .description(dto.getDescription())
                                .categoryUuid(getCategoryUuid(Receipt.CATEGORY_TYPE, dto.getCategory()))
                                .accountUuid(getAccountUuid(dto.getAccount()))
                                .shopUuid(getShopUuid(dto.getShop()))
                                .items(asReceiptItems(dto))
                                .build()
                );
    }

    private List<AddReceiptItemData> asReceiptItems(final AddReceiptDto dto) {
        final Map<String, String> addedProductCategories = new HashMap<>();
        final Map<String, AddReceiptItemProduct> addedProducts = new HashMap<>();
        return dto.getItems()
                .stream()
                .map(
                        itemDto -> AddReceiptItemData.builder()
                                .cost(itemDto.getCost())
                                .tags(itemDto.getTags())
                                .product(getExistingOrCreateProductUuid(addedProducts, addedProductCategories, itemDto))
                                .amount(
                                        AddReceiptItemAmount.builder()
                                                .count(itemDto.getAmount().getCount())
                                                .unit(itemDto.getAmount().getUnit())
                                                .build()
                                )
                                .build()
                )
                .collect(Collectors.toList());
    }

    /**
     * Gets product uuid.
     * <p>
     * Users one of:
     * - provided product uuid
     * - newly created product uuid
     * - product uuid used to create product within same receipt
     * </p>
     *
     * @param addedProducts          - products added within single add receipt request
     * @param addedProductCategories - product categories added within single add receipt request
     * @param itemDto                - add receipt item dto
     * @return product for receipt item
     */
    private AddReceiptItemProduct getExistingOrCreateProductUuid(final Map<String, AddReceiptItemProduct> addedProducts, final Map<String, String> addedProductCategories, final AddReceiptItemDto itemDto) {
        final AddReceiptProductDto product = itemDto.getProduct();
        return product
                .getUuid()
                .map(uuid -> AddReceiptItemProduct.builder().uuid(uuid).build())
                .orElseGet(createProduct(addedProducts, addedProductCategories, product));

    }

    private Supplier<AddReceiptItemProduct> createProduct(final Map<String, AddReceiptItemProduct> addedProducts, final Map<String, String> addedProductCategories, final AddReceiptProductDto product) {
        return () -> addedProducts.computeIfAbsent(
                product.getName().orElseThrow(WatsonException.supplier("Can't create receipt item product when name is missing")),
                productName -> {
                    final String categoryUuid = product
                            .getCategory()
                            .map(category -> createProductCategoryUuid(addedProductCategories, category))
                            .orElse(null);
                    final Event addProductEvent = addProductCommand.addProduct(
                            asAddProduct(productName, categoryUuid)
                    );
                    return AddReceiptItemProduct
                            .builder()
                            .uuid(addProductEvent.getStreamId())
                            .categoryUuid(categoryUuid)
                            .build();
                }
        );
    }

    private String createProductCategoryUuid(final Map<String, String> addedProductCategories, final AddReceiptCategoryDto category) {
        return category
                .getUuid()
                .orElseGet(
                        () -> addedProductCategories.computeIfAbsent(
                                category.getName().orElseThrow(WatsonException.supplier("Can't create category without neither uuid, nor name")),
                                categoryName -> getCategoryUuid(ReceiptItem.CATEGORY_TYPE, category)
                        )
                );
    }

    private AddProductData asAddProduct(final String key, final String categoryUuid) {
        return AddProductData.builder()
                .name(key)
                .categoryUuid(categoryUuid)
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

    private Function<AddReceiptCategoryDto, Optional<AddCategoryData>> asAddCategory(final String categoryType) {
        return dto -> dto.getName().map(name -> AddCategoryData.builder().type(categoryType).name(name).build());
    }

    private String getAccountUuid(final AddReceiptAccountDto dto) {
        return getUuidOrCreateObject(
                dto,
                AddReceiptAccountDto::getUuid,
                this::asAddAccount,
                addAccountCommand::addAccount
        );
    }

    private Optional<AddAccountData> asAddAccount(final AddReceiptAccountDto dto) {
        return dto.getName().map(name -> AddAccountData.builder().name(name).build());
    }

    private String getShopUuid(final AddReceiptShopDto dto) {
        return getUuidOrCreateObject(
                dto,
                AddReceiptShopDto::getUuid,
                this::asAddShop,
                addShopCommand::addShop
        );
    }

    private Optional<AddShopData> asAddShop(final AddReceiptShopDto dto) {
        return dto.getName().map(name -> AddShopData.builder().name(name).build());
    }

    private <T, M> String getUuidOrCreateObject(
            final T dto,
            final Function<T, Optional<String>> uuidExtractor,
            final Function<T, Optional<M>> objectMapper,
            final Function<M, Event> uuidGenerator) {
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

