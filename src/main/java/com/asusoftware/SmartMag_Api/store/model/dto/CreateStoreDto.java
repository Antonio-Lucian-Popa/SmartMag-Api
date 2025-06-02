package com.asusoftware.SmartMag_Api.store.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateStoreDto {

    @NotBlank
    private String name;

    private String address;

    @NotNull
    private UUID companyId;
}
