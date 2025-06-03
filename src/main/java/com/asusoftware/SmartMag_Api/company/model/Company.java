package com.asusoftware.SmartMag_Api.company.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "company")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Reprezintă o companie înregistrată în platformă")
public class Company {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    @Schema(description = "Identificatorul unic al companiei", example = "7e98b4d6-8f90-4b76-a5d9-70f5cb1b8e3d")
    private UUID id;

    @Column(nullable = false)
    @Schema(description = "Numele companiei", example = "SC Exemplu SRL", required = true)
    private String name;

    @Column(nullable = false, unique = true)
    @Schema(description = "Codul Unic de Înregistrare (CUI)", example = "RO12345678", required = true)
    private String cui;
}

