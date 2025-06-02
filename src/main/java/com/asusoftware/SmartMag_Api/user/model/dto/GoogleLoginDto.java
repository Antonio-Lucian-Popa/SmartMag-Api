package com.asusoftware.SmartMag_Api.user.model.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GoogleLoginDto {

    @NotBlank
    private String idToken; // token-ul JWT primit de la Google
}
