package com.gea.app.adminUser;

import com.gea.app.adminUser.dto.AdminUserCreateRequestDTO;
import com.gea.app.adminUser.dto.AdminUserReponseDTO;
import com.gea.app.adminUser.entity.AdminUser;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminUserService {
    @Autowired
    private AdminUserRepository adminUserRepository;
    @Autowired
    private final ModelMapper modelMapper;

    public AdminUserService(AdminUserRepository adminUserRepository, ModelMapper modelMapper) {
        this.adminUserRepository = adminUserRepository;
        this.modelMapper = modelMapper;
    }


    public List<AdminUserReponseDTO> getAdminUsers() {
        List<AdminUser> adminUsers = adminUserRepository.findAll();
        if (adminUsers.isEmpty()) {
            return null;
        }
        // Map each AdminUser to AdminUserResponseDTO
        List<AdminUserReponseDTO> adminUserResponseDTOs = adminUsers.stream()
                .map(adminUser -> modelMapper.map(adminUser, AdminUserReponseDTO.class))
                .collect(Collectors.toList());
        return adminUserResponseDTOs;
    }

    public AdminUserReponseDTO getAdminUserById(UUID id) {
        return adminUserRepository.findById(id)
                .map(adminUser -> modelMapper.map(adminUser, AdminUserReponseDTO.class))
                .orElse(null);
    }

    @Transactional
    public AdminUserReponseDTO createAdminUser(AdminUserCreateRequestDTO adminUserCreateRequestDTO) {

        AdminUser adminUser = modelMapper.map(adminUserCreateRequestDTO, AdminUser.class);

        AdminUser savedAdminUser = adminUserRepository.save(adminUser);

        AdminUserReponseDTO adminUserReponseDTO = modelMapper.map(savedAdminUser, AdminUserReponseDTO.class);

        return adminUserReponseDTO;
    }
}
