package com.example.app.loyalty.application;

/**
 * Public Application Service Interface for Loyalty Module
 * 
 * <p>This interface defines the public API for the Loyalty module.
 * Other modules should use this interface to interact with Loyalty module functionality.
 * 
 * <p><b>Module Boundary:</b> This is the public contract for cross-module communication.
 */
public interface LoyaltyApplicationService {
    
    /**
     * Earn loyalty points for an order
     * 
     * @param userId the user ID
     * @param orderId the order ID
     * @param points the points to earn
     * @param description description of the transaction
     */
    void earnPoints(Long userId, Long orderId, Integer points, String description);
    
    /**
     * Confirm a pending points transaction
     * 
     * @param transactionId the transaction ID
     */
    void confirmPointsTransaction(Long transactionId);
}

