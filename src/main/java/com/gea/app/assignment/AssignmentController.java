package com.gea.app.assignment;

import com.gea.app.assignment.dto.AssignmentCreateRequestDTO;
import com.gea.app.assignment.dto.AssignmentResponseDTO;
import com.gea.app.assignment.dto.AssignmentWrapper;
import com.gea.app._shared.model.dto.ApiResponse;

import jakarta.persistence.EntityExistsException;
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
@RequestMapping("/api/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    @Autowired
    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<AssignmentWrapper<List<AssignmentResponseDTO>>>> getAllAssignments() {
        List<AssignmentResponseDTO> assignments = assignmentService.getAllAssignments();
        AssignmentWrapper<List<AssignmentResponseDTO>> wrapper = new AssignmentWrapper<>(assignments);
        return ResponseEntity.ok(new ApiResponse<>(true, wrapper));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AssignmentWrapper<AssignmentResponseDTO>>> createAssignment(@Valid @RequestBody AssignmentCreateRequestDTO createRequestDTO) {
        try {
            AssignmentResponseDTO createdAssignment = assignmentService.createAssignment(createRequestDTO);
            AssignmentWrapper<AssignmentResponseDTO> wrapper = new AssignmentWrapper<>(createdAssignment);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(createdAssignment.getId())
                    .toUri();

            return ResponseEntity.created(location).body(new ApiResponse<>(true, wrapper));
        } catch (EntityNotFoundException | EntityExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, Collections.singletonList(e.getMessage())));
        }
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<ApiResponse<AssignmentWrapper<AssignmentResponseDTO>>> completeAssignment(@PathVariable UUID id) {
        try {
            AssignmentResponseDTO completedAssignment = assignmentService.completeAssignment(id);
            AssignmentWrapper<AssignmentResponseDTO> wrapper = new AssignmentWrapper<>(completedAssignment);
            return ResponseEntity.ok(new ApiResponse<>(true, wrapper));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, Collections.singletonList(e.getMessage())));
        } catch (IllegalStateException e) {
            // Ini untuk menangani kasus jika assignment sudah selesai
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, Collections.singletonList(e.getMessage())));
        }
    }
}