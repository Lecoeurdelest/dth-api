package com.example.app.orders.repository;

import com.example.app.orders.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByUserId(Long userId, Pageable pageable);
    List<Order> findByUserIdAndStatus(Long userId, Order.OrderStatus status);
    long countByStatus(Order.OrderStatus status);

    @Query(value = "SELECT * FROM orders_orders o WHERE o.worker_id = :workerId AND o.status IN :statuses " +
           "AND o.scheduled_at IS NOT NULL AND o.duration_minutes IS NOT NULL " +
           "AND o.scheduled_at < :endTime AND DATE_ADD(o.scheduled_at, INTERVAL o.duration_minutes MINUTE) > :startTime",
           nativeQuery = true)
    List<Order> findConflictingOrdersForWorker(@Param("workerId") Long workerId,
                                               @Param("startTime") LocalDateTime startTime,
                                               @Param("endTime") LocalDateTime endTime,
                                               @Param("statuses") List<Order.OrderStatus> statuses);
}


