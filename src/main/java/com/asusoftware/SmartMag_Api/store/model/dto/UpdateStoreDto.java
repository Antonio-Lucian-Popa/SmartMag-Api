package com.asusoftware.SmartMag_Api.store.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateStoreDto {

    @NotBlank
    private String name;
    private String address;
}
