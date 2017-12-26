package net.lipecki.watson.product;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class GetProductsTest extends ProductStoreBaseTest {

    private GetProductsQuery uut;

    @Before
    public void setUp() {
        uut = new GetProductsQuery(productStore);
    }

    @Test
    public void shouldGetAllProducts() {
        final String expectedProductName = "expectedProductName";

        // given
        eventStore.storeEvent(
                Product.PRODUCT_STREAM,
                ProductAdded
                        .builder()
                        .name(expectedProductName)
                        .build()
        );

        // when
        final List<Product> products = uut.getProducts();

        // then
        assertThat(products)
                .isNotNull()
                .isNotEmpty();
        assertThat(products)
                .extracting(Product::getName)
                .contains(expectedProductName);
    }

}
