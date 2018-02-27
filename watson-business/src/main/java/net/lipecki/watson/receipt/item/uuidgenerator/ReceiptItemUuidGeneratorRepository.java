package net.lipecki.watson.receipt.item.uuidgenerator;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiptItemUuidGeneratorRepository extends JpaRepository<ReceiptItemUuidGeneratorEntity, Long> {

    long countByReceiptUuid(String receiptUuid);

}
