package com.example.app.profile.application;

import com.example.app.profile.domain.UserProfile;
import com.example.app.profile.dto.ProfileDto;
import com.example.app.profile.dto.UpdateProfileRequest;
import com.example.app.profile.mapper.ProfileMapper;
import com.example.app.profile.repository.UserProfileRepository;
import com.example.app.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserProfileRepository userProfileRepository;
    private final ProfileMapper profileMapper;

    @Transactional(readOnly = true)
    public ProfileDto getProfileByUserId(Long userId) {
        return userProfileRepository.findByUserId(userId)
                .map(profileMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found for user id: " + userId));
    }

    @Transactional
    public ProfileDto updateProfile(Long userId, UpdateProfileRequest request) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseGet(() -> UserProfile.builder().userId(userId).build());

        if (request.getFirstName() != null) {
            profile.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            profile.setLastName(request.getLastName());
        }
        if (request.getBio() != null) {
            profile.setBio(request.getBio());
        }
        if (request.getAvatarUrl() != null) {
            profile.setAvatarUrl(request.getAvatarUrl());
        }

        profile = userProfileRepository.save(profile);
        return profileMapper.toDto(profile);
    }
}


