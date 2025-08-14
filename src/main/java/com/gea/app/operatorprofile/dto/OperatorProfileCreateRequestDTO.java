package com.gea.app.operatorprofile.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class OperatorProfileCreateRequestDTO {

    @NotNull(message = "userId cannot be null")
    private UUID userId;

    @NotNull(message = "unitId cannot be null")
    private UUID unitId;
}