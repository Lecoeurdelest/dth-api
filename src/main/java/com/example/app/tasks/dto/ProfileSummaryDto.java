package com.example.app.tasks.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileSummaryDto {
    private String firstName;
    private String lastName;
    private String avatarUrl;
    private String email;
}


