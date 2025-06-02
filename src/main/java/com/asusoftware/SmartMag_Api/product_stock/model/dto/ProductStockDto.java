package com.asusoftware.SmartMag_Api.product_stock.model.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ProductStockDto {
    private UUID id;
    private UUID productId;
    private UUID storeId;
    private int quantity;
}
