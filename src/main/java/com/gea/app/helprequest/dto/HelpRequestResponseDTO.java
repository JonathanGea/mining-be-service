package com.gea.app.helprequest.dto;

import com.gea.app.unit.dto.UnitResponseDTO;
import com.gea.app.user.dto.UserResponse;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class HelpRequestResponseDTO {
    private UUID id;
    private UserResponse requester;
    private UnitResponseDTO targetUnit;
    private String details;
    private String status;
    private Instant createdAt;
}