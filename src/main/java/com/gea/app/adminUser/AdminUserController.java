package com.gea.app.adminUser;

import com.gea.app.adminUser.dto.AdminUserCreateRequestDTO;
import com.gea.app.adminUser.dto.AdminUserReponseDTO;
import com.gea.app.adminUser.dto.AdminUsersWrapper;
import com.gea.app.shared.model.dto.ApiResponse;
import com.gea.app.shared.util.RequestIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


@RequestMapping("/api/admin-users")
@RestController
public class AdminUserController {


    @Autowired
    private AdminUserService adminUserService;

    @Autowired

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }


    @GetMapping
    public ResponseEntity<ApiResponse<AdminUsersWrapper>> getAllUserAdmins() {

        try {
            // Fetch data from service
            List<AdminUserReponseDTO> adminUserReponseDTO = adminUserService.getAdminUsers();

            AdminUsersWrapper dataWrapper = new AdminUsersWrapper(adminUserReponseDTO);

            ApiResponse<AdminUsersWrapper> response = new ApiResponse<>(true,dataWrapper);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            List<String> errors = Collections.singletonList(e.getMessage());

            ApiResponse<AdminUsersWrapper> response = new ApiResponse<>(false,errors);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AdminUsersWrapper<AdminUserReponseDTO>>> getUserAdminById(@PathVariable UUID id) {
        try {
            AdminUserReponseDTO adminUserReponseDTO = adminUserService.getAdminUserById(id);

            if (adminUserReponseDTO == null) {
                // Jika user tidak ditemukan, kembalikan kode status 404 Not Found
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, Collections.singletonList("User with id " + id + " not found")));
            }

            AdminUsersWrapper<AdminUserReponseDTO> dataWrapper = new AdminUsersWrapper<>(adminUserReponseDTO);
            ApiResponse<AdminUsersWrapper<AdminUserReponseDTO>> response = new ApiResponse<>(true, dataWrapper);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            List<String> errors = Collections.singletonList(e.getMessage());
            ApiResponse<AdminUsersWrapper<AdminUserReponseDTO>> response = new ApiResponse<>(false, errors);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AdminUsersWrapper<AdminUserReponseDTO>>> saveUserAdmin(@Valid @RequestBody AdminUserCreateRequestDTO adminUserCreateRequestDTO) {

        try {
            AdminUserReponseDTO adminUserReponseDTO = adminUserService.createAdminUser(adminUserCreateRequestDTO);
            AdminUsersWrapper<AdminUserReponseDTO> dataWrapper = new AdminUsersWrapper<>(adminUserReponseDTO);

            ApiResponse<AdminUsersWrapper<AdminUserReponseDTO>> response = new ApiResponse<>(true,dataWrapper);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(adminUserReponseDTO.getId())
                    .toUri();

            return ResponseEntity.created(location).body(response);

        } catch (Exception e) {
            List<String> errors = Collections.singletonList(e.getMessage());

            ApiResponse<AdminUsersWrapper<AdminUserReponseDTO>> response = new ApiResponse<>(false,errors);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


}
