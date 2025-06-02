package com.asusoftware.SmartMag_Api.product_stock.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "product_stock")
public class ProductStock {
    @Id
    private UUID id;

    private UUID productId;
    private UUID storeId;
    private Integer quantity;
}

