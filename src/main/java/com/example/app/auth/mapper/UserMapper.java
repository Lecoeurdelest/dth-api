package com.example.app.auth.mapper;

import com.example.app.auth.domain.User;
import com.example.app.auth.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "password", ignore = true)
    UserDto toDto(User user);
}


