package com.example.app.auth.application;

import com.example.app.auth.domain.RefreshToken;
import com.example.app.auth.domain.User;
import com.example.app.auth.dto.*;
import com.example.app.auth.mapper.UserMapper;
import com.example.app.auth.repository.RefreshTokenRepository;
import com.example.app.auth.repository.UserRepository;
import com.example.app.shared.exception.BadRequestException;
import com.example.app.shared.exception.UnauthorizedException;
import com.example.app.shared.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthApplicationService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final UserMapper userMapper;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Validate password match
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already exists");
        }
        if (request.getPhone() != null && userRepository.existsByPhone(request.getPhone())) {
            throw new BadRequestException("Phone number already exists");
        }

        // Use mapper to create user entity
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);

        user = userRepository.save(user);

        String accessToken = tokenProvider.generateAccessToken(user.getId().toString());
        String refreshToken = tokenProvider.generateRefreshToken(user.getId().toString());
        
        // Save refresh token
        saveRefreshToken(user.getId(), refreshToken);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(3600L) // 1 hour in seconds
                .user(userMapper.toDto(user))
                .build();
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        User user;
        
        // Find user based on loginType
        switch (request.getLoginType()) {
            case EMAIL:
                user = userRepository.findByEmail(request.getUsername())
                        .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));
                break;
            case USERNAME:
                user = userRepository.findByUsername(request.getUsername())
                        .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));
                break;
            case PHONE:
                user = userRepository.findByPhone(request.getUsername())
                        .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));
                break;
            default:
                throw new BadRequestException("Invalid login type");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        if (!user.getEnabled()) {
            throw new UnauthorizedException("Account is disabled");
        }

        String accessToken = tokenProvider.generateAccessToken(user.getId().toString());
        String refreshToken = tokenProvider.generateRefreshToken(user.getId().toString());
        
        // Save refresh token
        saveRefreshToken(user.getId(), refreshToken);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(3600L) // 1 hour in seconds
                .user(userMapper.toDto(user))
                .build();
    }

    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        // Validate token format
        if (!tokenProvider.validateToken(request.getRefreshToken())) {
            throw new UnauthorizedException("Invalid refresh token");
        }

        // Check if token exists in database
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new UnauthorizedException("Refresh token not found"));

        // Check if token is expired
        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new UnauthorizedException("Refresh token expired");
        }

        User user = userRepository.findById(refreshToken.getUserId())
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        if (!user.getEnabled()) {
            throw new UnauthorizedException("Account is disabled");
        }

        // Delete old refresh token
        refreshTokenRepository.delete(refreshToken);

        // Generate new tokens
        String accessToken = tokenProvider.generateAccessToken(user.getId().toString());
        String newRefreshToken = tokenProvider.generateRefreshToken(user.getId().toString());
        
        // Save new refresh token
        saveRefreshToken(user.getId(), newRefreshToken);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .expiresIn(3600L) // 1 hour in seconds
                .user(userMapper.toDto(user))
                .build();
    }
    
    @Transactional
    public void logout(String refreshToken) {
        refreshTokenRepository.findByToken(refreshToken)
                .ifPresent(refreshTokenRepository::delete);
    }
    
    private void saveRefreshToken(Long userId, String token) {
        Date expirationDate = tokenProvider.getExpirationDateFromToken(token);
        LocalDateTime expiresAt = expirationDate.toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDateTime();
        
        RefreshToken refreshToken = RefreshToken.builder()
                .userId(userId)
                .token(token)
                .expiresAt(expiresAt)
                .build();
        
        refreshTokenRepository.save(refreshToken);
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
    
    // Implementation of AuthApplicationService interface
    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new com.example.app.shared.exception.ResourceNotFoundException("User not found with id: " + userId));
        return userMapper.toDto(user);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean userExists(Long userId) {
        return userRepository.existsById(userId);
    }
}


