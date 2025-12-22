package com.example.app.orders.application;

import com.example.app.orders.dto.OrderDto;

/**
 * Public Application Service Interface for Orders Module
 * 
 * <p>This interface defines the public API for the Orders module.
 * Other modules should use this interface to interact with Orders module functionality.
 * 
 * <p><b>Module Boundary:</b> This is the public contract for cross-module communication.
 */
public interface OrderApplicationService {
    
    /**
     * Get order by ID
     * 
     * @param orderId the order ID
     * @return OrderDto containing order information
     * @throws com.example.app.shared.exception.ResourceNotFoundException if order not found
     */
    OrderDto getOrderById(Long orderId);
    
    /**
     * Check if order exists and is completed
     * 
     * @param orderId the order ID
     * @return true if order exists and is completed
     */
    boolean isOrderCompleted(Long orderId);
}

