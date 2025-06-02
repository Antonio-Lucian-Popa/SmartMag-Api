package com.asusoftware.SmartMag_Api.user.model.dto;

import com.asusoftware.SmartMag_Api.user.model.UserRole;
import lombok.Data;

import java.util.UUID;

@Data
public class UserDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private UserRole role;
    private UUID companyId;
    private UUID keycloakId;
}
