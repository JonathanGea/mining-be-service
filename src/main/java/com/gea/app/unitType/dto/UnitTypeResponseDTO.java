package com.gea.app.unitType.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnitTypeResponseDTO {
    private UUID id;
    private String name;
}