package com.example.app.contact.mapper;

import com.example.app.contact.domain.ContactMessage;
import com.example.app.contact.dto.ContactRequest;
import com.example.app.contact.dto.ContactResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContactMapper {
    ContactMessage toEntity(ContactRequest request);
    ContactResponse toResponse(ContactMessage message);
}


