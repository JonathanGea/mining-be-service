package com.gea.app.helprequest;

import com.gea.app.helprequest.dto.HelpRequestCreateRequestDTO;
import com.gea.app.helprequest.dto.HelpRequestResponseDTO;
import com.gea.app.helprequest.dto.HelpRequestWrapper;
import com.gea.app._shared.model.dto.ApiResponse;

import jakarta.persistence.EntityNotFoundException;
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
@RequestMapping("/api/help-requests")
public class HelpRequestController {

    private final HelpRequestService helpRequestService;

    @Autowired
    public HelpRequestController(HelpRequestService helpRequestService) {
        this.helpRequestService = helpRequestService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<HelpRequestWrapper<List<HelpRequestResponseDTO>>>> getAllHelpRequests() {
        List<HelpRequestResponseDTO> requests = helpRequestService.getAllHelpRequests();
        HelpRequestWrapper<List<HelpRequestResponseDTO>> wrapper = new HelpRequestWrapper<>(requests);
        return ResponseEntity.ok(new ApiResponse<>(true, wrapper));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HelpRequestWrapper<HelpRequestResponseDTO>>> getHelpRequestById(@PathVariable UUID id) {
        HelpRequestResponseDTO requestDTO = helpRequestService.getHelpRequestById(id);
        if (requestDTO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, Collections.singletonList("HelpRequest with id " + id + " not found")));
        }
        HelpRequestWrapper<HelpRequestResponseDTO> wrapper = new HelpRequestWrapper<>(requestDTO);
        return ResponseEntity.ok(new ApiResponse<>(true, wrapper));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<HelpRequestWrapper<HelpRequestResponseDTO>>> createHelpRequest(@Valid @RequestBody HelpRequestCreateRequestDTO createRequestDTO) {
        try {
            HelpRequestResponseDTO createdRequest = helpRequestService.createHelpRequest(createRequestDTO);
            HelpRequestWrapper<HelpRequestResponseDTO> wrapper = new HelpRequestWrapper<>(createdRequest);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(createdRequest.getId())
                    .toUri();

            return ResponseEntity.created(location).body(new ApiResponse<>(true, wrapper));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, Collections.singletonList(e.getMessage())));
        }
    }
}