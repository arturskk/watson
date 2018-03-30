package net.lipecki.watson.receipt;

import net.lipecki.watson.producer.AddProducerData;
import net.lipecki.watson.product.AddProductData;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AddProducerForReceiptProductTest extends AddReceiptWithDependenciesBaseTest {

    @Test
    public void shouldAddProductWithoutProducer() {
        mockAddProduct(
                AddProductData.builder().name(PRODUCT_NAME).build(),
                PRODUCT_UUID,
                productAdded()
        );

        // given
        final AddReceiptProductDto product = product(dto -> dto.name(PRODUCT_NAME));
        final AddReceiptItemDto.AddReceiptItemDtoBuilder item = item(dto -> dto.product(product));

        // when
        addReceipt(dto -> dto.item(item.build()));

        // then
        assertThat(receipt().getItems())
                .hasSize(1)
                .extracting(AddReceiptItemData::getProductUuid)
                .containsExactly(PRODUCT_UUID);
    }

    @Test
    public void shouldAddProductProducer() {
        mockAddProduct(
                AddProductData
                        .builder()
                        .name(PRODUCT_NAME)
                        .producerUuid(PRODUCER_UUID)
                        .build(),
                PRODUCT_UUID,
                productAdded(result -> result.producerUuid(PRODUCER_UUID))
        );

        // given
        final AddReceiptProductDto product = product(dto -> {
            dto.name(PRODUCT_NAME);
            dto.producer(producer(producer -> producer.uuid(PRODUCER_UUID)));
        });
        final AddReceiptItemDto.AddReceiptItemDtoBuilder item = item(dto -> dto.product(product));

        // when
        addReceipt(dto -> dto.item(item.build()));

        // then
        assertThat(receipt().getItems())
                .hasSize(1)
                .extracting(AddReceiptItemData::getProduct)
                .extracting(AddReceiptItemProduct::getProducerUuid)
                .containsExactly(PRODUCER_UUID);
    }

    @Test
    public void shouldAddDynamicProducerForProduct() {
        mockAddProducer(
                AddProducerData
                        .builder()
                        .name(PRODUCER_NAME)
                        .build(),
                PRODUCER_UUID,
                producerAdded()
        );
        mockAddProduct(
                AddProductData
                        .builder()
                        .name(PRODUCT_NAME)
                        .producerUuid(PRODUCER_UUID)
                        .build(),
                PRODUCT_UUID,
                productAdded(result -> result.producerUuid(PRODUCER_UUID))
        );

        // given
        final AddReceiptProductDto product = product(dto -> {
            dto.name(PRODUCT_NAME);
            dto.producer(producer(producer -> producer.name(PRODUCER_NAME)));
        });
        final AddReceiptItemDto.AddReceiptItemDtoBuilder item = item(dto -> dto.product(product));

        // when
        addReceipt(dto -> dto.item(item.build()));

        // then
        assertThat(receipt().getItems())
                .hasSize(1)
                .extracting(AddReceiptItemData::getProduct)
                .extracting(AddReceiptItemProduct::getProducerUuid)
                .containsExactly(PRODUCER_UUID);
    }

}
