package com.gea.app.helprequest;

import com.gea.app.helprequest.entity.HelpRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface HelpRequestRepository extends JpaRepository<HelpRequest, UUID> {
}