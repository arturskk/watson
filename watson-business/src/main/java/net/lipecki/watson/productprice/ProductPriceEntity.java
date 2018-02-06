package net.lipecki.watson.productprice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String categoryUuid;
    private String productUuid;
    private String shopUuid;
    private String receiptUuid;

}
