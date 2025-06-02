package com.asusoftware.SmartMag_Api.company.model.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CompanyDto {
    private UUID id;
    private String name;
    private String cui;
}

