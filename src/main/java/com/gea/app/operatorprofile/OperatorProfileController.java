package com.gea.app.operatorprofile;

import com.gea.app.operatorprofile.dto.OperatorProfileCreateRequestDTO;
import com.gea.app.operatorprofile.dto.OperatorProfileResponseDTO;
import com.gea.app.operatorprofile.dto.OperatorProfileWrapper;
import com.gea.app.shared.model.dto.ApiResponse;
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
@RequestMapping("/api/operator-profiles")
public class OperatorProfileController {

    private final OperatorProfileService operatorProfileService;

    @Autowired
    public OperatorProfileController(OperatorProfileService operatorProfileService) {
        this.operatorProfileService = operatorProfileService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<OperatorProfileWrapper<List<OperatorProfileResponseDTO>>>> getAllOperatorProfiles() {
        try {
            List<OperatorProfileResponseDTO> profiles = operatorProfileService.getAllOperatorProfiles();
            OperatorProfileWrapper<List<OperatorProfileResponseDTO>> dataWrapper = new OperatorProfileWrapper<>(profiles);
            ApiResponse<OperatorProfileWrapper<List<OperatorProfileResponseDTO>>> response = new ApiResponse<>(true, dataWrapper);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OperatorProfileWrapper<OperatorProfileResponseDTO>>> getOperatorProfileById(@PathVariable UUID id) {
        try {
            OperatorProfileResponseDTO profileDTO = operatorProfileService.getOperatorProfileById(id);
            if (profileDTO == null) {
                ApiResponse<OperatorProfileWrapper<OperatorProfileResponseDTO>> response = new ApiResponse<>(false, Collections.singletonList("OperatorProfile with id " + id + " not found"));
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            OperatorProfileWrapper<OperatorProfileResponseDTO> dataWrapper = new OperatorProfileWrapper<>(profileDTO);
            ApiResponse<OperatorProfileWrapper<OperatorProfileResponseDTO>> response = new ApiResponse<>(true, dataWrapper);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OperatorProfileWrapper<OperatorProfileResponseDTO>>> createOperatorProfile(@Valid @RequestBody OperatorProfileCreateRequestDTO createRequestDTO) {
        try {
            OperatorProfileResponseDTO createdProfile = operatorProfileService.createOperatorProfile(createRequestDTO);
            OperatorProfileWrapper<OperatorProfileResponseDTO> dataWrapper = new OperatorProfileWrapper<>(createdProfile);
            ApiResponse<OperatorProfileWrapper<OperatorProfileResponseDTO>> response = new ApiResponse<>(true, dataWrapper);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(createdProfile.getId())
                    .toUri();

            return ResponseEntity.created(location).body(response);
        } catch (Exception e) {
            // Ini akan menangkap EntityNotFoundException dari service
            return buildErrorResponse(e, HttpStatus.BAD_REQUEST);
        }
    }

    // Helper method untuk mengurangi duplikasi kode error handling
    private <T> ResponseEntity<ApiResponse<T>> buildErrorResponse(Exception e, HttpStatus status) {
        ApiResponse<T> response = new ApiResponse<>(false, Collections.singletonList(e.getMessage()));
        return ResponseEntity.status(status).body(response);
    }
}