package com.asusoftware.SmartMag_Api.product.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateProductDto {

    @NotBlank
    private String name;

    @NotBlank
    private String barcode;

    private String unit;

    private boolean isSgr;

    private Double sgrValue;

    @NotNull
    private UUID storeId;

}
