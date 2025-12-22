package com.example.app.services.application;

import com.example.app.services.dto.ServiceDto;
import com.example.app.shared.util.PageUtil;

import java.util.List;

/**
 * Public Application Service Interface for Services Module
 * 
 * <p>This interface defines the public API for the Services module.
 * Other modules should use this interface to interact with Services module functionality.
 * 
 * <p><b>Module Boundary:</b> This is the public contract for cross-module communication.
 * Implementations should be in the same package but are not exposed.
 */
public interface ServiceApplicationService {
    
    /**
     * Get service by ID
     * 
     * @param id the service ID
     * @return ServiceDto containing service information
     * @throws com.example.app.shared.exception.ResourceNotFoundException if service not found
     */
    ServiceDto getServiceById(Long id);
    
    /**
     * Get all services with pagination
     * 
     * @param page page number (0-indexed)
     * @param size page size
     * @param sortBy sort field
     * @param sortDir sort direction (ASC/DESC)
     * @return paginated response with services
     */
    PageUtil.PageResponse<ServiceDto> getAllServices(int page, int size, String sortBy, String sortDir);
    
    /**
     * Get services by category
     * 
     * @param category the category name
     * @return list of services in the category
     */
    List<ServiceDto> getServicesByCategory(String category);
    
    /**
     * Update service rating (called by Reviews module)
     * 
     * @param serviceId the service ID
     * @param newRating the new average rating
     * @param reviewCount the total review count
     */
    void updateServiceRating(Long serviceId, Double newRating, Long reviewCount);
}
