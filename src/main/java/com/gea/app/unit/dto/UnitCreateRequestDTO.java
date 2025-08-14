package com.gea.app.unit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

@Data
public class UnitCreateRequestDTO {

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Status cannot be blank")
    private String status;

    @NotNull(message = "unitTypeId cannot be null")
    private UUID unitTypeId; // Saat membuat, kita hanya perlu mengirimkan ID dari UnitType
}