package com.gea.app.helprequest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

@Data
public class HelpRequestCreateRequestDTO {

    @NotNull(message = "requesterId cannot be null")
    private UUID requesterId;

    @NotNull(message = "unitId cannot be null")
    private UUID unitId;

    @NotBlank(message = "Details cannot be blank")
    private String details;
}