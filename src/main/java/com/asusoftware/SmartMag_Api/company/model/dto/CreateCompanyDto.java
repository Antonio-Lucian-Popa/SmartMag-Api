package com.asusoftware.SmartMag_Api.company.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Obiect folosit pentru crearea sau actualizarea unei companii")
public class CreateCompanyDto {

    @NotBlank
    @Schema(description = "Numele companiei", example = "SC Exemplu SRL", required = true)
    private String name;

    @NotBlank
    @Schema(description = "Codul Unic de Înregistrare (CUI)", example = "RO12345678", required = true)
    private String cui;
}
