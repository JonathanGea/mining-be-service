package com.gea.app.auth;

import com.gea.app.auth.dto.AuthResponse;
import com.gea.app.auth.dto.LoginRequest;
import com.gea.app.auth.dto.RegisterRequest;
import com.gea.app._shared.util.JwtService;
import com.gea.app.user.dto.UserResponse;
import com.gea.app.user.enum_.Role;
import com.gea.app.user.entity.User;
import com.gea.app.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;

    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new RuntimeException("Email already registered");
        }
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new RuntimeException("Username already taken");
        }

        // map DTO -> Entity pakai ModelMapper (org.modelmapper.ModelMapper)
        User user = modelMapper.map(req, User.class);

        // handle field khusus yg tak boleh plain-copy
        user.setPassword(passwordEncoder.encode(req.getPassword())); // encode manual
        user.setRole(Role.USER);                                     // default role

        var saved = userRepository.save(user);

        var hydrated = userRepository.findByIdWithUnit(saved.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found after register"));

        String token = jwtService.generateToken(
                hydrated.getUsername(),
                Map.of("role", hydrated.getRole().name(), "sub", hydrated.getId().toString())
        );

        UserResponse userDto = modelMapper.map(hydrated, UserResponse.class);
        return new AuthResponse(token, userDto);
    }

    public AuthResponse login(LoginRequest req) {
        var authToken = new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword());
        authenticationManager.authenticate(authToken);

        var user = userRepository.findByUsernameWithUnit(req.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        String token = jwtService.generateToken(
                user.getUsername(),
                Map.of("role", user.getRole().name(), "sub", user.getId().toString())
        );

        UserResponse userDto = modelMapper.map(user, UserResponse.class);
        return new AuthResponse(token, userDto);
    }
}

