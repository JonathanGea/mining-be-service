package com.gea.app.assignment;

import com.gea.app.assignment.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, UUID> {
    // Cek apakah sebuah help request sudah punya assignment
    boolean existsByHelpRequestId(UUID helpRequestId);
}