package com.example.app.contact.mapper;

import com.example.app.contact.domain.ContactMessage;
import com.example.app.contact.dto.ContactRequest;
import com.example.app.contact.dto.ContactResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ContactMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "read", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ContactMessage toEntity(ContactRequest request);
    
    ContactResponse toResponse(ContactMessage message);
}


