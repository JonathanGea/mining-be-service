package com.gea.app.assignment.dto;

import com.gea.app.helpRequest.dto.HelpRequestResponseDTO;
import com.gea.app.user.dto.UserResponse;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class AssignmentResponseDTO {
    private UUID id;
    private HelpRequestResponseDTO helpRequest;
    private UserResponse helper;
    private Instant startedAt;
    private Instant createdAt;
    private Instant completedAt;
}