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

/**
 * Controller pentru gestionarea produselor din compania utilizatorului (OWNER sau MANAGER).
 * Permite crearea, listarea și ștergerea produselor.
 */
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * Creează un produs nou asociat companiei utilizatorului curent.
     * Poate fi folosit pentru produse precum echipamente, consumabile, etc.
     *
     * @param dto Obiect cu informațiile produsului (nume, descriere, cantitate, etc.)
     * @param principal Jwt token-ul utilizatorului logat
     * @return Produsul creat ca DTO
     */
    @PostMapping
    public ResponseEntity<ProductDto> create(
            @Valid @RequestBody CreateProductDto dto,
            @AuthenticationPrincipal Jwt principal
    ) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(productService.createProduct(dto, keycloakId));
    }

    /**
     * Returnează toate produsele asociate companiei utilizatorului curent.
     * Este util pentru inventar, afișare stocuri sau alte operațiuni.
     *
     * @param principal Jwt token-ul utilizatorului logat
     * @return Lista de produse sub formă de DTO-uri
     */
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAll(
            @AuthenticationPrincipal Jwt principal
    ) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(productService.getAllForCompany(keycloakId));
    }

    /**
     * Șterge un produs existent din compania utilizatorului curent.
     * Operațiunea este permisă doar dacă produsul aparține companiei și utilizatorul are drepturi.
     *
     * @param productId ID-ul produsului de șters
     * @param principal Jwt token-ul utilizatorului logat
     * @return 204 No Content dacă ștergerea a avut succes
     */
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
