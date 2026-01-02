package com.example.demo.service;

import com.example.demo.dto.LoginResponse;
import com.example.demo.entity.RefreshToken;
import com.example.demo.entity.RegisterRequest;
import com.example.demo.entity.User;
import com.example.demo.repository.RefreshTokenRepository;
import com.example.demo.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public User register(String tenantId, RegisterRequest request) {

        if (userRepository.existsByTenantIdAndEmail(tenantId, request.getEmail())) {
            throw new RuntimeException("User already exists");
        }

        User user = new User();
        user.setTenantId(tenantId);
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userRepository.save(user);
    }


    public LoginResponse login(String tenantId, String email, String password) {

        User user = userRepository
                .findByTenantIdAndEmail(tenantId, email)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String accessToken = jwtService.generateAccessToken(user.getId(), tenantId);
        String refreshToken = jwtService.generateRefreshToken();

        RefreshToken rt = new RefreshToken();
        rt.setUserId(user.getId());
        rt.setTenantId(tenantId);
        rt.setToken(refreshToken);
        rt.setExpiry(Instant.now().plus(7, ChronoUnit.DAYS));
        rt.setRevoked(false);

        refreshTokenRepository.save(rt);

        return new LoginResponse(accessToken, refreshToken);
    }


    public String refresh(String refreshToken) {

        RefreshToken stored = refreshTokenRepository
                .findByTokenAndRevokedFalse(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (stored.getExpiry().isBefore(Instant.now())) {
            throw new RuntimeException("Refresh token expired");
        }

        stored.setRevoked(true);
        refreshTokenRepository.save(stored);

        String newRefresh = jwtService.generateRefreshToken();

        RefreshToken next = new RefreshToken();
        next.setUserId(stored.getUserId());
        next.setTenantId(stored.getTenantId());
        next.setToken(newRefresh);
        next.setExpiry(Instant.now().plusMillis(7L * 24 * 60 * 60 * 1000));
        next.setRevoked(false);

        refreshTokenRepository.save(next);

        return jwtService.generateAccessToken(
                stored.getUserId(),
                stored.getTenantId()
        );
    }


    public void logout(String refreshToken) {

        String hashed = jwtService.hashToken(refreshToken);

        refreshTokenRepository.findByTokenAndRevokedFalse(hashed)
                .ifPresent(refreshTokenRepository::delete);
    }
}
