package com.asusoftware.SmartMag_Api.store.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class AssignUserToStoreDto {

    @NotNull
    private UUID userId;

    @NotNull
    private UUID storeId;
}
