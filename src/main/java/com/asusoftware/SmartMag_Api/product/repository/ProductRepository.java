package com.asusoftware.SmartMag_Api.product.repository;

import com.asusoftware.SmartMag_Api.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    Optional<Product> findByBarcode(String barcode);
    List<Product> findByCompanyId(UUID companyId);
    List<Product> findByStoreId(UUID storeId);

}
