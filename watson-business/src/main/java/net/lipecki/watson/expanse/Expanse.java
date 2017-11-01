package net.lipecki.watson.expanse;

import net.lipecki.watson.account.Account;
import net.lipecki.watson.budget.Budget;
import net.lipecki.watson.category.Category;

import java.time.LocalDate;
import java.util.List;

public interface Expanse {

    /**
     * Gets expanse date.
     * @return expanse date
     */
    LocalDate getDate();

    /**
     * Gets expanse UUID.
     * @return expanse unique uuid
     */
    String getUuid();

    /**
     * Gets budget UUID.
     * Represents concrete parent budget for expanse, used to distinct entities within multi-tenant system.
     * @return
     */
    Budget getBudget();

    /**
     * Gets account used to pay for expanse.
     * @return UUID of account
     */
    Account getAccount();

    /**
     * Gets expanse sub items.
     * @return expanse items
     */
    List<? extends ExpanseItem> getItems();

    /**
     *
     * @return
     */
    Category getCategory();

    // typ wydatku?
    // tagi?

}
