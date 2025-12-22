package com.example.app.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginRequest {
    
    @NotBlank(message = "Username, email, or phone is required")
    private String username;
    
    @NotBlank(message = "Password is required")
    private String password;
    
    @NotNull(message = "Login type is required")
    private LoginType loginType = LoginType.EMAIL;
    
    public enum LoginType {
        USERNAME,
        EMAIL,
        PHONE
    }
}
