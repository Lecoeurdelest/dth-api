package com.example.app.contact.api;

import com.example.app.contact.application.ContactService;
import com.example.app.contact.dto.ContactRequest;
import com.example.app.contact.dto.ContactResponse;
import com.example.app.shared.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contact")
@RequiredArgsConstructor
@Tag(name = "Contact", description = "Contact APIs")
public class ContactController {

    private final ContactService contactService;

    @PostMapping
    @Operation(summary = "Submit contact form")
    public ResponseEntity<ApiResponse<ContactResponse>> submitContact(@Valid @RequestBody ContactRequest request) {
        ContactResponse response = contactService.submitContactMessage(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Contact message submitted successfully"));
    }
}


