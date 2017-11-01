package net.lipecki.watson.expanse;

import net.lipecki.watson.category.Category;

import java.util.List;

public interface ExpanseItem {

    /**
     * Gets expanse item short description.
     * - one line text
     * - used to display within expanse summary
     * @return short description
     */
    String getDescription();

    /**
     * Gets expanse item cost.
     * @return cost
     */
    ExpanseCost getCost();

    /**
     * Gets expanse item category id.
     * @return category uuid
     */
    Category getCategory();

    /**
     * Gets expanse item tags.
     * @return tags
     */
    List<String> getTags();

}
