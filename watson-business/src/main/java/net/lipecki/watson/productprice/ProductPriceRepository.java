package net.lipecki.watson.productprice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.stream.Stream;

public interface ProductPriceRepository extends JpaRepository<ProductPriceEntity, Long> {

    @Modifying
    @Query("update product_prices p set p.categoryUuid = :category where p.productUuid = :uuid")
    void updateProductCategory(@Param("uuid") String uuid, @Param("category") String category);

    Stream<ProductPriceEntity> findAllByCategoryUuid(final String uuid);

}
