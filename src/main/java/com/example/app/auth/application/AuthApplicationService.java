package com.example.app.auth.application;

import com.example.app.auth.dto.UserDto;

/**
 * Public Application Service Interface for Auth Module
 * 
 * <p>This interface defines the public API for the Auth module.
 * Other modules should use this interface to interact with Auth module functionality.
 * 
 * <p><b>Module Boundary:</b> This is the public contract for cross-module communication.
 * Implementations should be in the same package but are not exposed.
 */
public interface AuthApplicationService {
    
    /**
     * Get user information by ID
     * 
     * @param userId the user ID
     * @return UserDto containing user information
     * @throws com.example.app.shared.exception.ResourceNotFoundException if user not found
     */
    UserDto getUserById(Long userId);
    
    /**
     * Check if user exists by ID
     * 
     * @param userId the user ID
     * @return true if user exists, false otherwise
     */
    boolean userExists(Long userId);
}

