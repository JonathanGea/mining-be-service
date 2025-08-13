package com.gea.app.adminUser.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserCreateRequestDTO {

    @NotBlank(message = "Username tidak boleh kosong")
    @Size(min = 3, max = 50, message = "Username harus antara 3 dan 50 karakter")
    private String name;

    @NotBlank(message = "Username tidak boleh kosong")
    @Size(min = 8, message = "Password minimal harus 8 karakter")
    private String password;
}


