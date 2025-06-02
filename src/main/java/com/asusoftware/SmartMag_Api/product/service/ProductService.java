package com.asusoftware.SmartMag_Api.product.service;

import com.asusoftware.SmartMag_Api.exception.ResourceNotFoundException;
import com.asusoftware.SmartMag_Api.product.model.Product;
import com.asusoftware.SmartMag_Api.product.model.dto.CreateProductDto;
import com.asusoftware.SmartMag_Api.product.model.dto.ProductDto;
import com.asusoftware.SmartMag_Api.product.repository.ProductRepository;
import com.asusoftware.SmartMag_Api.store.model.Store;
import com.asusoftware.SmartMag_Api.store.repository.StoreRepository;
import com.asusoftware.SmartMag_Api.user.model.User;
import com.asusoftware.SmartMag_Api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final ModelMapper mapper;

    public ProductDto createProduct(CreateProductDto dto, UUID keycloakId) {
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Store store = storeRepository.findById(dto.getStoreId())
                .orElseThrow(() -> new ResourceNotFoundException("Store not found"));

        if (!store.getCompanyId().equals(user.getCompanyId())) {
            throw new IllegalArgumentException("Access denied to this store");
        }

        Product product = mapper.map(dto, Product.class);
        product.setCompanyId(user.getCompanyId());

        product = productRepository.save(product);
        return mapper.map(product, ProductDto.class);
    }

    public ProductDto updateProduct(UUID productId, CreateProductDto dto, UUID keycloakId) {
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (!product.getCompanyId().equals(user.getCompanyId())) {
            throw new IllegalArgumentException("Access denied to this product");
        }

        product.setName(dto.getName());
        product.setBarcode(dto.getBarcode());
        product.setUnit(dto.getUnit());
        product.setSgr(dto.isSgr());
        product.setSgrValue(dto.getSgrValue());

        product = productRepository.save(product);
        return mapper.map(product, ProductDto.class);
    }

    public List<ProductDto> getAllForStore(UUID storeId, UUID keycloakId) {
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found"));

        if (!store.getCompanyId().equals(user.getCompanyId())) {
            throw new IllegalArgumentException("Access denied to this store");
        }

        return productRepository.findByStoreId(storeId).stream()
                .map(p -> mapper.map(p, ProductDto.class))
                .collect(Collectors.toList());
    }

    public List<ProductDto> getAllForCompany(UUID keycloakId) {
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return productRepository.findByCompanyId(user.getCompanyId()).stream()
                .map(p -> mapper.map(p, ProductDto.class))
                .collect(Collectors.toList());
    }

    public void deleteProduct(UUID productId, UUID keycloakId) {
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (!product.getCompanyId().equals(user.getCompanyId())) {
            throw new IllegalArgumentException("Access denied to this product");
        }

        productRepository.delete(product);
    }
}

