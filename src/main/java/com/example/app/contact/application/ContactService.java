package com.example.app.contact.application;

import com.example.app.contact.domain.ContactMessage;
import com.example.app.contact.dto.ContactRequest;
import com.example.app.contact.dto.ContactResponse;
import com.example.app.contact.mapper.ContactMapper;
import com.example.app.contact.repository.ContactMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactMessageRepository contactMessageRepository;
    private final ContactMapper contactMapper;

    @Transactional
    public ContactResponse submitContactMessage(ContactRequest request) {
        ContactMessage message = contactMapper.toEntity(request);
        message = contactMessageRepository.save(message);
        return contactMapper.toResponse(message);
    }
}


