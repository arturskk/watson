package net.lipecki.watson.product;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.lipecki.watson.amount.AmountUnit;
import net.lipecki.watson.category.Category;
import net.lipecki.watson.producer.Producer;

import java.util.Optional;

@EqualsAndHashCode(of = "uuid")
@Data
@Builder
public class Product {

    public static final String PRODUCT_STREAM = "_product";

    private String uuid;
    private String name;
    private Category category;
    private AmountUnit defaultUnit;
    private Producer producer;

    public Optional<AmountUnit> getDefaultUnitOptional() {
        return Optional.ofNullable(defaultUnit);
    }

    public Optional<Producer> getProducerOptional() {
        return Optional.ofNullable(producer);
    }

}
