package com.gea.app.operatorprofile.dto;

import com.gea.app.unit.dto.UnitResponseDTO;
// Asumsi ada UserResponseDTO sederhana di package user.dto
import com.gea.app.user.dto.UserResponse; 
import lombok.Data;

import java.util.UUID;

@Data
public class OperatorProfileResponseDTO {
    private UUID id;
    private UserResponse user; // Objek user yang bersarang
    private UnitResponseDTO unit; // Objek unit yang bersarang
}