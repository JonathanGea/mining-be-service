package com.gea.app.adminUser;

import java.util.UUID;

import com.gea.app.adminUser.entity.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminUserRepository extends JpaRepository<AdminUser, UUID> {
}