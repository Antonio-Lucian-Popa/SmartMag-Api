package com.asusoftware.SmartMag_Api.product_stock.service;

import com.asusoftware.SmartMag_Api.product_stock.model.ProductStock;
import com.asusoftware.SmartMag_Api.product_stock.model.dto.ProductStockDto;
import com.asusoftware.SmartMag_Api.product_stock.model.dto.UpdateProductStockDto;
import com.asusoftware.SmartMag_Api.product_stock.repository.ProductStockRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductStockService {

    private final ProductStockRepository stockRepository;
    private final ModelMapper mapper;

    public ProductStockDto adjustStock(UpdateProductStockDto dto) {
        ProductStock stock = stockRepository.findByProductIdAndStoreId(dto.getProductId(), dto.getStoreId())
                .orElse(ProductStock.builder()
                        .productId(dto.getProductId())
                        .storeId(dto.getStoreId())
                        .quantity(0)
                        .build());

        int newQty = stock.getQuantity() + dto.getQuantity();
        if (newQty < 0) throw new IllegalArgumentException("Stoc insuficient pentru scădere.");

        stock.setQuantity(newQty);
        stock = stockRepository.save(stock);
        return mapper.map(stock, ProductStockDto.class);
    }

    public List<ProductStockDto> getStockForStore(UUID storeId) {
        return stockRepository.findByStoreId(storeId).stream()
                .map(stock -> mapper.map(stock, ProductStockDto.class))
                .collect(Collectors.toList());
    }

    public List<ProductStockDto> getStockForProduct(UUID productId) {
        return stockRepository.findByProductId(productId).stream()
                .map(stock -> mapper.map(stock, ProductStockDto.class))
                .collect(Collectors.toList());
    }
}
