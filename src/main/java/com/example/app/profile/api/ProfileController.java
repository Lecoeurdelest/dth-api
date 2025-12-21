package com.example.app.profile.api;

import com.example.app.profile.application.ProfileService;
import com.example.app.profile.dto.ProfileDto;
import com.example.app.profile.dto.UpdateProfileRequest;
import com.example.app.shared.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
@Tag(name = "Profile", description = "Profile management APIs")
@SecurityRequirement(name = "bearerAuth")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    @Operation(summary = "Get current user profile")
    public ResponseEntity<ApiResponse<ProfileDto>> getProfile(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        ProfileDto response = profileService.getProfileByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping
    @Operation(summary = "Update current user profile")
    public ResponseEntity<ApiResponse<ProfileDto>> updateProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateProfileRequest request) {
        Long userId = Long.parseLong(authentication.getName());
        ProfileDto response = profileService.updateProfile(userId, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Profile updated successfully"));
    }
}


