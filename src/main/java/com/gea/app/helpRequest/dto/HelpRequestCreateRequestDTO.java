package com.gea.app.helpRequest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

@Data
public class HelpRequestCreateRequestDTO {

    @NotNull(message = "requesterId cannot be null")
    private UUID requesterId;

    @NotNull(message = "unitTypeId cannot be null")
    private UUID unitTypeId;

    @NotBlank(message = "Details cannot be blank")
    private String details;
}