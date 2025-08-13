package com.gea.app.adminUser.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;
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


