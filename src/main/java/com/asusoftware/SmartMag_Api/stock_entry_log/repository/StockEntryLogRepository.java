package com.asusoftware.SmartMag_Api.stock_entry_log.repository;

import com.asusoftware.SmartMag_Api.stock_entry_log.model.StockEntryLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StockEntryLogRepository extends JpaRepository<StockEntryLog, UUID> {
    List<StockEntryLog> findByProductId(UUID productId);
    List<StockEntryLog> findByStoreId(UUID storeId);
    List<StockEntryLog> findByProductIdAndStoreId(UUID productId, UUID storeId);
}
