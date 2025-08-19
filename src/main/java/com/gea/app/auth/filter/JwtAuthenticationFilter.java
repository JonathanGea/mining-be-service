package com.gea.app.auth.filter;

import com.gea.app._shared.util.JwtService;
import com.gea.app.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Biarkan preflight
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        final String header = request.getHeader(AUTH_HEADER);
        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        final String token = header.substring(BEARER_PREFIX.length());

        // Subject = username
        String subject;
        try {
            subject = jwtService.extractUsername(token);
        } catch (Exception e) {
            chain.doFilter(request, response);
            return;
        }

        // Jangan proses kalau sudah ada authentication
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            chain.doFilter(request, response);
            return;
        }

        // Ambil uid (opsional) dari klaim
        UUID userId = null;
        try {
            String uid = jwtService.extractClaim(token, claims -> claims.get("uid", String.class));
            if (uid != null && !uid.isBlank()) {
                userId = UUID.fromString(uid);
            }
        } catch (Exception ignored) { }

        Optional<com.gea.app.user.entity.User> userOpt = Optional.empty();

        // Prioritas: cari by ID dari uid
        if (userId != null) {
            userOpt = userRepository.findById(userId);
        }

        // Fallback: cari by username (subject)
        if (userOpt.isEmpty() && subject != null && !subject.isBlank()) {
            userOpt = userRepository.findByUsername(subject);
        }

        if (userOpt.isPresent()) {
            var user = userOpt.get();
            if (jwtService.isTokenValid(token, user.getUsername())) {
                var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        chain.doFilter(request, response);
    }
}
