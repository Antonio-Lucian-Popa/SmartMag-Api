package com.asusoftware.SmartMag_Api.product_stock.repository;

import com.asusoftware.SmartMag_Api.product_stock.model.ProductStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductStockRepository extends JpaRepository<ProductStock, UUID> {
    Optional<ProductStock> findByProductIdAndStoreId(UUID productId, UUID storeId);
    List<ProductStock> findByStoreId(UUID storeId);
    List<ProductStock> findByProductId(UUID productId);
}