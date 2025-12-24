package com.example.app.workers.application;

import com.example.app.auth.domain.Role;
import com.example.app.auth.domain.User;
import com.example.app.orders.domain.Order;
import com.example.app.orders.repository.OrderRepository;
import com.example.app.workers.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkerAvailabilityService {

    private final WorkerRepository workerRepository;
    private final OrderRepository orderRepository;

    /**
     * Check if a worker is available for a given time slot
     *
     * @param workerId the worker ID
     * @param scheduledAt the scheduled time (can be null for general availability)
     * @param durationMinutes the duration in minutes (default 120)
     * @return true if worker is available, false otherwise
     */
    public boolean isWorkerAvailable(Long workerId, LocalDateTime scheduledAt, Integer durationMinutes) {
        // Check if worker exists and has WORKER role
        User worker = workerRepository.findByIdAndRole(workerId, Role.WORKER)
                .orElse(null);

        if (worker == null) {
            return false;
        }

        // If no scheduled time specified, assume worker is generally available
        if (scheduledAt == null) {
            return true;
        }

        // Check for conflicting orders
        int duration = durationMinutes != null ? durationMinutes : 120; // Default 2 hours
        LocalDateTime endTime = scheduledAt.plusMinutes(duration);

        // Find any overlapping orders for this worker that are not cancelled or completed
        List<Order> conflictingOrders = orderRepository.findConflictingOrdersForWorker(
                workerId, scheduledAt, endTime, List.of(Order.OrderStatus.PENDING, Order.OrderStatus.CONFIRMED, Order.OrderStatus.IN_PROGRESS));

        return conflictingOrders.isEmpty();
    }

    /**
     * Check if a worker is available for a given time slot
     * Overload method for convenience
     */
    public boolean isWorkerAvailable(Long workerId) {
        return isWorkerAvailable(workerId, null, null);
    }
}
