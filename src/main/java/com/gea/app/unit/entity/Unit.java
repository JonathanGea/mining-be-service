package com.gea.app.unit.entity;

import com.gea.app.unitType.entity.UnitType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "units")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Unit {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "status", nullable = false)
    private String status;

    // Relasi Many-To-One ke UnitType
    // Banyak "Unit" bisa memiliki satu "UnitType" yang sama.
    @ManyToOne(fetch = FetchType.LAZY) // LAZY loading adalah praktik yang baik
    @JoinColumn(name = "unit_type_id", nullable = false) // Ini adalah nama kolom foreign key di tabel 'units'
    private UnitType unitType;
}