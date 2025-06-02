package com.asusoftware.SmartMag_Api.product.controller;

import com.asusoftware.SmartMag_Api.product.model.dto.CreateProductDto;
import com.asusoftware.SmartMag_Api.product.model.dto.ProductDto;
import com.asusoftware.SmartMag_Api.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDto> create(
            @Valid @RequestBody CreateProductDto dto,
            @AuthenticationPrincipal Jwt principal
    ) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(productService.createProduct(dto, keycloakId));
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAll(
            @AuthenticationPrincipal Jwt principal
    ) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(productService.getAllForCompany(keycloakId));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID productId,
            @AuthenticationPrincipal Jwt principal
    ) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        productService.deleteProduct(productId, keycloakId);
        return ResponseEntity.noContent().build();
    }
}