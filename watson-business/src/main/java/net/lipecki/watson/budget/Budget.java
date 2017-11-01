package net.lipecki.watson.budget;

public interface Budget {

    /**
     * Gets budget UUID.
     * Used to distinguish multi-tenant entities.
     * @return
     */
    String getUuid();

    String getCurrency();

}
