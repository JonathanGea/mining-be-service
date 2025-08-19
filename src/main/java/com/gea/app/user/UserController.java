package com.gea.app.user;

import com.gea.app._shared.model.dto.ApiResponse;
import com.gea.app.user.dto.UserResponse;
import com.gea.app.user.dto.UserWrapper;
import com.gea.app.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

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

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> me(@AuthenticationPrincipal User principal) {
        var data = userService.getMe(principal);
        return ResponseEntity.ok(new ApiResponse<>(true, data));
    }
}
