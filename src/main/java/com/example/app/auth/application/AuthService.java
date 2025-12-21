package com.example.app.auth.application;

import com.example.app.auth.domain.User;
import com.example.app.auth.dto.*;
import com.example.app.auth.mapper.UserMapper;
import com.example.app.auth.repository.UserRepository;
import com.example.app.shared.exception.BadRequestException;
import com.example.app.shared.exception.UnauthorizedException;
import com.example.app.shared.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final UserMapper userMapper;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already exists");
        }
        if (request.getPhone() != null && userRepository.existsByPhone(request.getPhone())) {
            throw new BadRequestException("Phone number already exists");
        }

        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .build();

        user = userRepository.save(user);

        String accessToken = tokenProvider.generateAccessToken(user.getId().toString());
        String refreshToken = tokenProvider.generateRefreshToken(user.getId().toString());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(userMapper.toDto(user))
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getUsernameOrEmail())
                .orElseGet(() -> userRepository.findByUsername(request.getUsernameOrEmail())
                        .orElseThrow(() -> new UnauthorizedException("Invalid credentials")));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        if (!user.getEnabled()) {
            throw new UnauthorizedException("Account is disabled");
        }

        String accessToken = tokenProvider.generateAccessToken(user.getId().toString());
        String refreshToken = tokenProvider.generateRefreshToken(user.getId().toString());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(userMapper.toDto(user))
                .build();
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {
        if (!tokenProvider.validateToken(request.getRefreshToken())) {
            throw new UnauthorizedException("Invalid refresh token");
        }

        String userId = tokenProvider.getUserIdFromToken(request.getRefreshToken());
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        if (!user.getEnabled()) {
            throw new UnauthorizedException("Account is disabled");
        }

        String accessToken = tokenProvider.generateAccessToken(user.getId().toString());
        String refreshToken = tokenProvider.generateRefreshToken(user.getId().toString());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(userMapper.toDto(user))
                .build();
    }

    // Social login placeholders
    public AuthResponse loginWithGoogle(String googleId) {
        // Placeholder for Google OAuth implementation
        throw new BadRequestException("Google login not implemented yet");
    }

    public AuthResponse loginWithFacebook(String facebookId) {
        // Placeholder for Facebook OAuth implementation
        throw new BadRequestException("Facebook login not implemented yet");
    }
}


