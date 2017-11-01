package net.lipecki.watson.receipt;

import net.lipecki.watson.WatsonException;
import net.lipecki.watson.WatsonExceptionCode;
import net.lipecki.watson.account.AddAccountService;
import net.lipecki.watson.category.AddCategoryService;
import net.lipecki.watson.shop.AddShopService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;

@Service
public class AddReceiptWithDependenciesService {

    private final AddReceiptService addReceiptService;
    private final AddCategoryService addCategoryService;
    private final AddAccountService addAccountService;
    private final AddShopService addShopService;

    public AddReceiptWithDependenciesService(
            final AddReceiptService addReceiptService,
            final AddCategoryService addCategoryService,
            final AddAccountService addAccountService,
            final AddShopService addShopService) {
        this.addReceiptService = addReceiptService;
        this.addCategoryService = addCategoryService;
        this.addAccountService = addAccountService;
        this.addShopService = addShopService;
    }

    public String addReceipt(final AddReceiptDto addReceipt) {
        final String categoryUuid = getCategoryUuid(addReceipt.getCategory());
        final String accountUuid = getAccountUuid(addReceipt.getAccount());
        final String shopUuid = getShopUuid(addReceipt.getShop());
        return addReceiptService.addReceipt(
                AddReceipt
                        .builder()
                        .budgetUuid(addReceipt.getBudgetUuid())
                        .tags(new ArrayList<>(addReceipt.getTags()))
                        .date(LocalDate.parse(addReceipt.getDate()))
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
            return this.addCategoryService.addCategory(Receipt.CATEGORY_TYPE, category.getName().get());
        } else {
            throw new WatsonException(
                    WatsonExceptionCode.BAD_REQUEST,
                    "Neither uuid, nor new category name was present"
            ).put(
                    "category", category
            );
        }
    }

    private String getAccountUuid(final AddReceiptAccountDto account) {
        if (account.getUuid().isPresent()) {
            return account.getUuid().get();
        } else if (account.getName().isPresent()) {
            return this.addAccountService.addAccount(account.getName().get());
        } else {
            throw new WatsonException(
                    WatsonExceptionCode.BAD_REQUEST,
                    "Neither uuid, nor new account name was present"
            ).put(
                    "account", account
            );
        }
    }

    private String getShopUuid(final AddReceiptShopDto shop) {
        if (shop.getUuid().isPresent()) {
            return shop.getUuid().get();
        } else if (shop.getName().isPresent()) {
            return this.addShopService.addShop(shop.getName().get());
        } else {
            throw new WatsonException(
                    WatsonExceptionCode.BAD_REQUEST,
                    "Neither uuid, nor new shop name was present"
            ).put(
                    "shop", shop
            );
        }
    }

}

