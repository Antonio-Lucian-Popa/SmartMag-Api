package com.asusoftware.SmartMag_Api.product_stock.controller;

import com.asusoftware.SmartMag_Api.product_stock.model.dto.ProductStockDto;
import com.asusoftware.SmartMag_Api.product_stock.model.dto.UpdateProductStockDto;
import com.asusoftware.SmartMag_Api.product_stock.service.ProductStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller pentru gestionarea stocurilor produselor pe magazin.
 * Permite ajustarea stocului și afișarea stocurilor per magazin sau per produs.
 */
@RestController
@RequestMapping("/api/v1/stocks")
@RequiredArgsConstructor
public class ProductStockController {

    private final ProductStockService stockService;

    /**
     * Ajustează cantitatea unui produs într-un anumit magazin (adăugare sau scădere).
     * Poate fi folosit când se aduce marfă nouă, se face o corecție sau se consumă produse.
     *
     * @param dto Obiect cu ID-ul produsului, ID-ul magazinului și cantitatea de ajustat (+/-)
     * @return Obiectul DTO actualizat cu noul stoc
     */
    @PostMapping("/adjust")
    public ResponseEntity<ProductStockDto> adjustStock(@RequestBody UpdateProductStockDto dto) {
        return ResponseEntity.ok(stockService.adjustStock(dto));
    }

    /**
     * Returnează lista de produse și stocurile aferente pentru un anumit magazin.
     * Util pentru a vedea ce produse sunt disponibile într-un magazin.
     *
     * @param storeId ID-ul magazinului
     * @return Lista de stocuri pentru magazinul specificat
     */
    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<ProductStockDto>> getStockByStore(@PathVariable UUID storeId) {
        return ResponseEntity.ok(stockService.getStockForStore(storeId));
    }

    /**
     * Returnează lista de stocuri pentru un anumit produs în toate magazinele.
     * Util pentru a vedea unde este disponibil un anumit produs.
     *
     * @param productId ID-ul produsului
     * @return Lista de stocuri pentru produsul specificat
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductStockDto>> getStockByProduct(@PathVariable UUID productId) {
        return ResponseEntity.ok(stockService.getStockForProduct(productId));
    }
}

