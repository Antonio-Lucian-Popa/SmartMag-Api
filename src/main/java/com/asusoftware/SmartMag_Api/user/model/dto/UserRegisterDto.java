package com.asusoftware.SmartMag_Api.user.model.dto;

import com.asusoftware.SmartMag_Api.user.model.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class UserRegisterDto {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotNull
    private UserRole role;
}
