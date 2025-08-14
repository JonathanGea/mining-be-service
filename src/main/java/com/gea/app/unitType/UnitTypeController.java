package com.gea.app.unitType;


import com.gea.app.shared.model.dto.ApiResponse;
import com.gea.app.unitType.dto.UnitTypeCreateRequestDTO;
import com.gea.app.unitType.dto.UnitTypeResponseDTO;
import com.gea.app.unitType.dto.UnitTypesWrapper;
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
@RequestMapping("/api/unit-types")
public class UnitTypeController {

    private final UnitTypeService unitTypeService;

    @Autowired
    public UnitTypeController(UnitTypeService unitTypeService) {
        this.unitTypeService = unitTypeService;
    }

    /**
     * Endpoint untuk mendapatkan semua UnitType.
     * Method: GET
     * URL: /api/unit-types
     */
    @GetMapping
    public ResponseEntity<ApiResponse<UnitTypesWrapper<List<UnitTypeResponseDTO>>>> getAllUnitTypes() {
        try {
            // 1. Ambil data dari service
            List<UnitTypeResponseDTO> unitTypes = unitTypeService.getAllUnitTypes();

            // 2. Bungkus data list ke dalam UnitTypesWrapper
            UnitTypesWrapper<List<UnitTypeResponseDTO>> dataWrapper = new UnitTypesWrapper<>(unitTypes);

            // 3. Bungkus wrapper ke dalam ApiResponse
            ApiResponse<UnitTypesWrapper<List<UnitTypeResponseDTO>>> response = new ApiResponse<>(true, dataWrapper);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<UnitTypesWrapper<List<UnitTypeResponseDTO>>> response = new ApiResponse<>(false, Collections.singletonList(e.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Endpoint untuk mendapatkan satu UnitType berdasarkan ID.
     * Method: GET
     * URL: /api/unit-types/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UnitTypesWrapper<UnitTypeResponseDTO>>> getUnitTypeById(@PathVariable UUID id) {
        try {
            // 1. Ambil data dari service
            UnitTypeResponseDTO unitTypeDTO = unitTypeService.getUnitTypeById(id);

            // 2. Cek jika data tidak ditemukan, kembalikan 404 Not Found
            if (unitTypeDTO == null) {
                ApiResponse<UnitTypesWrapper<UnitTypeResponseDTO>> response = new ApiResponse<>(false, Collections.singletonList("UnitType with id " + id + " not found"));
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // 3. Bungkus data tunggal ke dalam UnitTypesWrapper
            UnitTypesWrapper<UnitTypeResponseDTO> dataWrapper = new UnitTypesWrapper<>(unitTypeDTO);
            ApiResponse<UnitTypesWrapper<UnitTypeResponseDTO>> response = new ApiResponse<>(true, dataWrapper);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<UnitTypesWrapper<UnitTypeResponseDTO>> response = new ApiResponse<>(false, Collections.singletonList(e.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Endpoint untuk membuat UnitType baru.
     * Method: POST
     * URL: /api/unit-types
     */
    @PostMapping
    public ResponseEntity<ApiResponse<UnitTypesWrapper<UnitTypeResponseDTO>>> createUnitType(@Valid @RequestBody UnitTypeCreateRequestDTO createRequestDTO) {
        try {
            // 1. Panggil service untuk membuat data baru
            UnitTypeResponseDTO createdUnitType = unitTypeService.createUnitType(createRequestDTO);

            // 2. Bungkus data yang baru dibuat ke dalam UnitTypesWrapper
            UnitTypesWrapper<UnitTypeResponseDTO> dataWrapper = new UnitTypesWrapper<>(createdUnitType);
            ApiResponse<UnitTypesWrapper<UnitTypeResponseDTO>> response = new ApiResponse<>(true, dataWrapper);

            // 3. Buat URI untuk header 'Location' sebagai best practice REST API
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(createdUnitType.getId())
                    .toUri();

            // 4. Kembalikan status 201 Created dengan body dan header location
            return ResponseEntity.created(location).body(response);
        } catch (Exception e) {
            // Menangani kemungkinan error seperti duplikasi nama (unique constraint violation)
            ApiResponse<UnitTypesWrapper<UnitTypeResponseDTO>> response = new ApiResponse<>(false, Collections.singletonList(e.getMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}