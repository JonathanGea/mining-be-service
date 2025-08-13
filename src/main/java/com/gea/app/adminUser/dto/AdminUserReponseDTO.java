package com.gea.app.adminUser.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserReponseDTO {
    private UUID id;
    private ZonedDateTime createdAt;
    private String password;
    private String name;
}


