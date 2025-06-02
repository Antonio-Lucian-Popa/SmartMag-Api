package com.asusoftware.SmartMag_Api.company.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCompanyDto {
    @NotBlank
    private String name;

    @NotBlank
    private String cui;
}
