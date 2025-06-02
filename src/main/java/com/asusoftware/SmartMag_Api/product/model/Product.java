package com.asusoftware.SmartMag_Api.product.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String barcode;

    private String unit; // e.g., "kg", "pcs"

    @Column(name = "is_sgr")
    private boolean isSgr = false;

    @Column(name = "sgr_value")
    private Double sgrValue = 0.0;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;
}

