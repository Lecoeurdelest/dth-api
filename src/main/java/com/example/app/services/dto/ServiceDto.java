package com.example.app.services.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Service DTO - Flexible data transfer object for Service entities
 * 
 * <p>This DTO provides flexibility for API responses and can be extended
 * with additional computed fields or nested objects as needed.
 * 
 * <p>Fields can be optional or nullable based on use case requirements.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDto {

    private Long id;
    private String name;
    private String description;
    private BigDecimal basePrice;
    private String imageUrl;
    private String category;
    private String details;
    private Boolean active;
    
    // Optional: Include timestamps if needed for admin/full details views
    // Can be excluded for simple list views to reduce payload size
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Flexible extension points - can add computed fields as needed:
    // - Double averageRating;        // From reviews module
    // - Long reviewCount;            // From reviews module
    // - List<String> tags;           // For filtering/search
    // - Map<String, Object> metadata; // For flexible additional data
}
