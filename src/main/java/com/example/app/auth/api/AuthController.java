package com.example.app.auth.api;

import com.example.app.auth.application.AuthService;
import com.example.app.auth.dto.*;
import com.example.app.shared.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication APIs")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request, HttpServletResponse httpResponse) {
        AuthResponse response = authService.register(request);
        // Set httpOnly cookies for access and refresh tokens
        if (response.getAccessToken() != null) {
            ResponseCookie accessCookie = ResponseCookie.from("accessToken", response.getAccessToken())
                    .path("/")
                    .httpOnly(true)
                    .secure(false)
                    .sameSite("Lax")
                    .maxAge(response.getExpiresIn() != null ? response.getExpiresIn() : 3600L)
                    .build();
            httpResponse.addHeader("Set-Cookie", accessCookie.toString());
        }
        if (response.getRefreshToken() != null) {
            ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", response.getRefreshToken())
                    .path("/")
                    .httpOnly(true)
                    .secure(false)
                    .sameSite("Lax")
                    .maxAge(60L * 60L * 24L)
                    .build();
            httpResponse.addHeader("Set-Cookie", refreshCookie.toString());
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "User registered successfully"));
    }

    @PostMapping("/login")
    @Operation(summary = "Login user")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request, HttpServletResponse httpResponse) {
        AuthResponse response = authService.login(request);
        if (response.getAccessToken() != null) {
            ResponseCookie accessCookie = ResponseCookie.from("accessToken", response.getAccessToken())
                    .path("/")
                    .httpOnly(true)
                    .secure(false)
                    .sameSite("Lax")
                    .maxAge(response.getExpiresIn() != null ? response.getExpiresIn() : 3600L)
                    .build();
            httpResponse.addHeader("Set-Cookie", accessCookie.toString());
        }
        if (response.getRefreshToken() != null) {
            ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", response.getRefreshToken())
                    .path("/")
                    .httpOnly(true)
                    .secure(false)
                    .sameSite("Lax")
                    .maxAge(60L * 60L * 24L)
                    .build();
            httpResponse.addHeader("Set-Cookie", refreshCookie.toString());
        }
        return ResponseEntity.ok(ApiResponse.success(response, "Login successful"));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest request, HttpServletResponse httpResponse) {
        AuthResponse response = authService.refreshToken(request);
        if (response.getAccessToken() != null) {
            ResponseCookie accessCookie = ResponseCookie.from("accessToken", response.getAccessToken())
                    .path("/")
                    .httpOnly(true)
                    .secure(false)
                    .sameSite("Lax")
                    .maxAge(response.getExpiresIn() != null ? response.getExpiresIn() : 3600L)
                    .build();
            httpResponse.addHeader("Set-Cookie", accessCookie.toString());
        }
        if (response.getRefreshToken() != null) {
            ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", response.getRefreshToken())
                    .path("/")
                    .httpOnly(true)
                    .secure(false)
                    .sameSite("Lax")
                    .maxAge(60L * 60L * 24L)
                    .build();
            httpResponse.addHeader("Set-Cookie", refreshCookie.toString());
        }
        return ResponseEntity.ok(ApiResponse.success(response, "Token refreshed successfully"));
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout user")
    public ResponseEntity<ApiResponse<Void>> logout(@Valid @RequestBody RefreshTokenRequest request, HttpServletResponse httpResponse) {
        authService.logout(request.getRefreshToken());
        // Clear cookies
        ResponseCookie clearAccess = ResponseCookie.from("accessToken", "")
                .path("/")
                .httpOnly(true)
                .maxAge(0)
                .build();
        ResponseCookie clearRefresh = ResponseCookie.from("refreshToken", "")
                .path("/")
                .httpOnly(true)
                .maxAge(0)
                .build();
        httpResponse.addHeader("Set-Cookie", clearAccess.toString());
        httpResponse.addHeader("Set-Cookie", clearRefresh.toString());
        return ResponseEntity.ok(ApiResponse.success(null, "Logout successful"));
    }

    @PostMapping("/logout-cookie")
    @Operation(summary = "Logout user using refresh token cookie")
    public ResponseEntity<ApiResponse<Void>> logoutCookie(@CookieValue(value = "refreshToken", required = false) String refreshToken,
                                                          HttpServletResponse httpResponse) {
        if (refreshToken != null && !refreshToken.isBlank()) {
            authService.logout(refreshToken);
        }
        ResponseCookie clearAccess = ResponseCookie.from("accessToken", "")
                .path("/")
                .httpOnly(true)
                .maxAge(0)
                .build();
        ResponseCookie clearRefresh = ResponseCookie.from("refreshToken", "")
                .path("/")
                .httpOnly(true)
                .maxAge(0)
                .build();
        httpResponse.addHeader("Set-Cookie", clearAccess.toString());
        httpResponse.addHeader("Set-Cookie", clearRefresh.toString());
        return ResponseEntity.ok(ApiResponse.success(null, "Logout successful"));
    }

    @PostMapping("/google")
    @Operation(summary = "Login with Google (placeholder)")
    public ResponseEntity<ApiResponse<AuthResponse>> loginWithGoogle(@RequestBody String googleId) {
        AuthResponse response = authService.loginWithGoogle(googleId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/facebook")
    @Operation(summary = "Login with Facebook (placeholder)")
    public ResponseEntity<ApiResponse<AuthResponse>> loginWithFacebook(@RequestBody String facebookId) {
        AuthResponse response = authService.loginWithFacebook(facebookId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}


