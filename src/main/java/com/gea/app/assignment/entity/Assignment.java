package com.gea.app.assignment.entity;

import com.gea.app.helpRequest.entity.HelpRequest;
import com.gea.app.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "assignments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Assignment {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    // Relasi One-to-One ke HelpRequest
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "help_request_id", unique = true, nullable = false)
    private HelpRequest helpRequest;

    // User yang ditugaskan (helper)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "helper_id", nullable = false)
    private User helper;

    @Column(name = "started_at")
    private Instant startedAt; // Akan null saat pertama kali dibuat

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "completed_at")
    private Instant completedAt; // Akan null saat pertama kali dibuat
}