package com.example.app.orders.repository;

import com.example.app.orders.domain.OrderReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderReviewRepository extends JpaRepository<OrderReview, Long> {
    Optional<OrderReview> findByOrderId(Long orderId);
    
    @Query("SELECT r FROM OrderReview r JOIN Order o ON r.orderId = o.id WHERE o.serviceId = :serviceId")
    List<OrderReview> findByServiceId(@Param("serviceId") Long serviceId);
}

