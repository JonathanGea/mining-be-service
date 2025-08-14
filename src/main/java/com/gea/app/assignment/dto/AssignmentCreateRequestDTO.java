package com.gea.app.assignment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

@Data
public class AssignmentCreateRequestDTO {

    @NotNull(message = "helpRequestId cannot be null")
    private UUID helpRequestId;

    @NotNull(message = "helperId cannot be null")
    private UUID helperId;
}