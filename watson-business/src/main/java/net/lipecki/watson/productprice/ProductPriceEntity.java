package net.lipecki.watson.productprice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity(name = "product_prices")
@Table(
        indexes = {
                @Index(name = "_idx_product_prices_id", columnList = "id"),
        }
)
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class ProductPriceEntity {

    @Id
    @GeneratedValue(generator = "_seq_product_prices_id")
    @SequenceGenerator(name = "_seq_product_prices_id", sequenceName = "_seq_product_prices_id", allocationSize = 1)
    private Long id;
    private String categoryUuid;
    private String productUuid;
    private String shopUuid;
    private String receiptUuid;
    private String date;
    private String pricePerUnit;
    private String unit;
    private String receiptItemUuid;

}
