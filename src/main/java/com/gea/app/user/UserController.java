package com.gea.app.user;

import com.gea.app._shared.model.dto.ApiResponse;
import com.gea.app.user.dto.AssignUnitRequest;
import com.gea.app.user.dto.UserResponse;
import com.gea.app.user.dto.UserWrapper;
import com.gea.app.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<UserWrapper<List<UserResponse>>>> getUsers() {
        var users = userService.getUsers();
        var wrapped = new UserWrapper<>(users);
        return ResponseEntity.ok(new ApiResponse<>(true, wrapped));
    }

    @PutMapping("/{userId}/unit")
    public ResponseEntity<ApiResponse<UserResponse>> assignUnitToUser(
            @PathVariable UUID userId,
            @RequestBody AssignUnitRequest req
    ) {
        var data = userService.assignUnitToUser(userId, req.getUnitId());
        return ResponseEntity.ok(new ApiResponse<>(true, data));
    }


    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> me(@AuthenticationPrincipal User principal) {
        var data = userService.getMe(principal);
        return ResponseEntity.ok(new ApiResponse<>(true, data));
    }
}
