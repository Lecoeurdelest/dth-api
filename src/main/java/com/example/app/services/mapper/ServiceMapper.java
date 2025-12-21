package com.example.app.services.mapper;

import com.example.app.services.dto.ServiceDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ServiceMapper {
    ServiceDto toDto(com.example.app.services.domain.Service service);
}

