package com.asusoftware.SmartMag_Api.company.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(description = "Obiect care conține informații despre o companie")
public class CompanyDto {

    @Schema(description = "Identificatorul unic al companiei", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
    private UUID id;

    @Schema(description = "Numele companiei", example = "SC Exemplu SRL")
    private String name;

    @Schema(description = "Codul Unic de Înregistrare (CUI)", example = "RO12345678")
    private String cui;
}

