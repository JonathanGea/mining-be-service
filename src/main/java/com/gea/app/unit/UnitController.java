package com.gea.app.unit;

import com.gea.app._shared.model.dto.ApiResponse;
import com.gea.app.unit.dto.UnitCreateRequestDTO;
import com.gea.app.unit.dto.UnitResponseDTO;
import com.gea.app.unit.dto.UnitWrapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/units")
public class UnitController {

    private final UnitService unitService;

    @Autowired
    public UnitController(UnitService unitService) {
        this.unitService = unitService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<UnitWrapper<List<UnitResponseDTO>>>> getAllUnits() {
        try {
            List<UnitResponseDTO> units = unitService.getAllUnits();
            UnitWrapper<List<UnitResponseDTO>> dataWrapper = new UnitWrapper<>(units);
            ApiResponse<UnitWrapper<List<UnitResponseDTO>>> response = new ApiResponse<>(true, dataWrapper);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<UnitWrapper<List<UnitResponseDTO>>> response = new ApiResponse<>(false, Collections.singletonList(e.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UnitWrapper<UnitResponseDTO>>> getUnitById(@PathVariable UUID id) {
        try {
            UnitResponseDTO unitDTO = unitService.getUnitById(id);
            if (unitDTO == null) {
                ApiResponse<UnitWrapper<UnitResponseDTO>> response = new ApiResponse<>(false, Collections.singletonList("Unit with id " + id + " not found"));
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            UnitWrapper<UnitResponseDTO> dataWrapper = new UnitWrapper<>(unitDTO);
            ApiResponse<UnitWrapper<UnitResponseDTO>> response = new ApiResponse<>(true, dataWrapper);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<UnitWrapper<UnitResponseDTO>> response = new ApiResponse<>(false, Collections.singletonList(e.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UnitWrapper<UnitResponseDTO>>> createUnit(@Valid @RequestBody UnitCreateRequestDTO createRequestDTO) {
        try {
            UnitResponseDTO createdUnit = unitService.createUnit(createRequestDTO);
            UnitWrapper<UnitResponseDTO> dataWrapper = new UnitWrapper<>(createdUnit);
            ApiResponse<UnitWrapper<UnitResponseDTO>> response = new ApiResponse<>(true, dataWrapper);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(createdUnit.getId())
                    .toUri();

            return ResponseEntity.created(location).body(response);
        } catch (Exception e) {
            ApiResponse<UnitWrapper<UnitResponseDTO>> response = new ApiResponse<>(false, Collections.singletonList(e.getMessage()));
            // Jika UnitType tidak ditemukan, exception akan dilempar dari service
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}