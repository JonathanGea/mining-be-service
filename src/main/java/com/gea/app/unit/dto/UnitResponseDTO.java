package com.gea.app.unit.dto;

import com.gea.app.unitType.dto.UnitTypeResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnitResponseDTO {
    private UUID id;
    private String name;
    private String status;
    private UnitTypeResponseDTO unitType; // Respons akan menyertakan objek UnitType
}