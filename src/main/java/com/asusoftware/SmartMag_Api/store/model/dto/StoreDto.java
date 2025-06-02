package com.asusoftware.SmartMag_Api.store.model.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class StoreDto {
    private UUID id;
    private String name;
    private String address;
    private UUID companyId;
}
