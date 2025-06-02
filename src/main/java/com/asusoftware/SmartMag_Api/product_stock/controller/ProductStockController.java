package com.asusoftware.SmartMag_Api.product_stock.controller;

import com.asusoftware.SmartMag_Api.product_stock.model.dto.ProductStockDto;
import com.asusoftware.SmartMag_Api.product_stock.model.dto.UpdateProductStockDto;
import com.asusoftware.SmartMag_Api.product_stock.service.ProductStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/stocks")
@RequiredArgsConstructor
public class ProductStockController {

    private final ProductStockService stockService;

    @PostMapping("/adjust")
    public ResponseEntity<ProductStockDto> adjustStock(@RequestBody UpdateProductStockDto dto) {
        return ResponseEntity.ok(stockService.adjustStock(dto));
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<ProductStockDto>> getStockByStore(@PathVariable UUID storeId) {
        return ResponseEntity.ok(stockService.getStockForStore(storeId));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductStockDto>> getStockByProduct(@PathVariable UUID productId) {
        return ResponseEntity.ok(stockService.getStockForProduct(productId));
    }
}

