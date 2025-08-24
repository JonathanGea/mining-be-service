package com.gea.app.assignment;

import com.gea.app.assignment.dto.AssignmentCreateRequestDTO;
import com.gea.app.assignment.dto.AssignmentResponseDTO;
import com.gea.app.assignment.entity.Assignment;
import com.gea.app.helpRequest.HelpRequestRepository;
import com.gea.app.helpRequest.entity.HelpRequest;
import com.gea.app.user.UserRepository;
import com.gea.app.user.entity.User;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final HelpRequestRepository helpRequestRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public AssignmentService(AssignmentRepository assignmentRepository, HelpRequestRepository helpRequestRepository,
            UserRepository userRepository, ModelMapper modelMapper) {
        this.assignmentRepository = assignmentRepository;
        this.helpRequestRepository = helpRequestRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public List<AssignmentResponseDTO> getAllAssignments() {
        return assignmentRepository.findAll().stream()
                .map(assignment -> modelMapper.map(assignment, AssignmentResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public AssignmentResponseDTO createAssignment(AssignmentCreateRequestDTO createRequestDTO) {
        // 1. Validasi bahwa HelpRequest belum di-assign
        if (assignmentRepository.existsByHelpRequestId(createRequestDTO.getHelpRequestId())) {
            throw new EntityExistsException(
                    "HelpRequest with id " + createRequestDTO.getHelpRequestId() + " is already assigned.");
        }

        // 2. Cari entitas HelpRequest dan Helper (User)
        HelpRequest helpRequest = helpRequestRepository.findById(createRequestDTO.getHelpRequestId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "HelpRequest with id " + createRequestDTO.getHelpRequestId() + " not found."));

        User helper = (User) userRepository.findById(createRequestDTO.getHelperId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "User (helper) with id " + createRequestDTO.getHelperId() + " not found."));

        // 3. Buat entitas Assignment baru
        Assignment newAssignment = Assignment.builder()
                .helpRequest(helpRequest)
                .helper(helper)
                .build(); // startedAt dan completedAt akan null

        // 4. Ubah status HelpRequest menjadi "ASSIGNED" (Logika Bisnis)
        helpRequest.setStatus("ASSIGNED");
        helpRequestRepository.save(helpRequest);

        // 5. Simpan assignment baru dan flush untuk mendapatkan createdAt
        Assignment savedAssignment = assignmentRepository.saveAndFlush(newAssignment);

        return modelMapper.map(savedAssignment, AssignmentResponseDTO.class);
    }

    @Transactional
    public AssignmentResponseDTO completeAssignment(UUID assignmentId) {
        // 1. Cari Assignment berdasarkan ID. Jika tidak ada, lempar error.
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new EntityNotFoundException("Assignment with id " + assignmentId + " not found."));

        // 2. Validasi: Jangan selesaikan assignment yang sudah selesai.
        if (assignment.getCompletedAt() != null) {
            throw new IllegalStateException("Assignment with id " + assignmentId + " has already been completed.");
        }

        // 3. Update field di Assignment
        assignment.setCompletedAt(Instant.now());

        // Jika Anda juga ingin menandai 'started_at' saat pertama kali diselesaikan
        // (jika belum ada)
        if (assignment.getStartedAt() == null) {
            assignment.setStartedAt(assignment.getCreatedAt()); // Atau Instant.now(), tergantung logika bisnis
        }

        // 4. Update status di HelpRequest terkait
        HelpRequest helpRequest = assignment.getHelpRequest();
        helpRequest.setStatus("COMPLETED"); // Atau "RESOLVED"

        // 5. Simpan perubahan. Karena ini @Transactional, Hibernate akan otomatis
        // menyimpan perubahan pada 'helpRequest' juga saat transaksi di-commit.
        // Kita save 'assignment' secara eksplisit untuk kejelasan.
        Assignment updatedAssignment = assignmentRepository.save(assignment);

        // 6. Map ke DTO untuk respons
        return modelMapper.map(updatedAssignment, AssignmentResponseDTO.class);
    }
}