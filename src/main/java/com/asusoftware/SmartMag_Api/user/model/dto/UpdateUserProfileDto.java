package com.asusoftware.SmartMag_Api.user.model.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserProfileDto {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;
}

