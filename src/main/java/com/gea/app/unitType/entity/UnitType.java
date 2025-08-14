package com.gea.app.unitType.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "unit_types") 
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnitType {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;
}