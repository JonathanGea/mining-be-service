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
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        var user = User.builder()
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(Role.USER)
                .build();

        var saved = userRepository.save(user);

        // Ambil ulang dengan fetch-join supaya unit ikut (meski null tetap aman)
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
        var authToken = new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword());
        authenticationManager.authenticate(authToken);

        var user = userRepository.findByEmailWithUnit(req.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        String token = jwtService.generateToken(
                user.getUsername(),
                Map.of("role", user.getRole().name(), "sub", user.getId().toString())
        );

        UserResponse userDto = modelMapper.map(user, UserResponse.class);
        return new AuthResponse(token, userDto);
    }
}
