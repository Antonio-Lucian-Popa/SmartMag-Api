package com.asusoftware.SmartMag_Api.stock_entry_log.model.dto;

import com.asusoftware.SmartMag_Api.stock_entry_log.model.StockEntryType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateStockEntryLogDto {

    @NotNull
    private UUID productId;

    @NotNull
    private UUID storeId;

    @NotNull
    private Integer quantity;

    @NotNull
    private StockEntryType type;

    private String reason;
}
