package com.gea.app.unit.dto;

import com.gea.app.unitType.dto.UnitTypeResponseDTO;
import lombok.Data;
import java.util.UUID;

@Data
public class UnitResponseDTO {
    private UUID id;
    private String name;
    private String status;
    private UnitTypeResponseDTO unitType; // Respons akan menyertakan objek UnitType
}