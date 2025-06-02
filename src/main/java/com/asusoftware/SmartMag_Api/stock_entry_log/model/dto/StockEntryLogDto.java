package com.asusoftware.SmartMag_Api.stock_entry_log.model.dto;

import com.asusoftware.SmartMag_Api.stock_entry_log.model.StockEntryType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class StockEntryLogDto {
    private UUID id;
    private UUID productId;
    private UUID storeId;
    private UUID userId;
    private Integer quantity;
    private StockEntryType type;
    private LocalDateTime date;
    private String reason;
}
