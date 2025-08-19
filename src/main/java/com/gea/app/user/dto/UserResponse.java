package com.gea.app.user.dto;

import com.gea.app.unit.dto.UnitResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserResponse {
    private UUID id;
    private String username;
    private String role; // "USER" / "ADMIN"
    private UnitResponseDTO unit;
}