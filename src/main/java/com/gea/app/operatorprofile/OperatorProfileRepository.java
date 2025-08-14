package com.gea.app.operatorprofile;

import com.gea.app.operatorprofile.entity.OperatorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OperatorProfileRepository extends JpaRepository<OperatorProfile, UUID> {
}