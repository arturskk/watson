package net.lipecki.watson.receipt.item.uuidgenerator;

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

@Entity(name = "receipt_item_uuid_generator")
@Table(
        indexes = {
                @Index(name = "_idx_receipt_item_uuid_generator_id", columnList = "id"),
        }
)
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class ReceiptItemUuidGeneratorEntity {

    @Id
    @GeneratedValue(generator = "_seq_receipt_item_uuid_generator_id")
    @SequenceGenerator(name = "_seq_receipt_item_uuid_generator_id", sequenceName = "_seq_receipt_item_uuid_generator_id", allocationSize = 1)
    private Long id;
    private String receiptUuid;
    private String itemOldUuid;
    private String itemNewUuid;

}
