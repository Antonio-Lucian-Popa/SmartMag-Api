package com.asusoftware.SmartMag_Api.product_stock.model.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UpdateProductStockDto {
    private UUID productId;
    private UUID storeId;
    private int quantity; // poate fi pozitiv sau negativ (adjust)
}