package com.example.app.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkerDto {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String phone;
    private String avatarUrl;
    private String skills; // JSON/text
}



