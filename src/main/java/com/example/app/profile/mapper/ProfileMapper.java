package com.example.app.profile.mapper;

import com.example.app.profile.domain.UserProfile;
import com.example.app.profile.dto.ProfileDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileDto toDto(UserProfile profile);
}


