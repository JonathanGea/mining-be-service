package com.gea.app.unitType;

import com.gea.app.unitType.entity.UnitType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UnitTypeRepository extends JpaRepository<UnitType, UUID> {
}