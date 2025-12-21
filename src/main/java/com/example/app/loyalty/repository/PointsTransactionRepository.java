package com.example.app.loyalty.repository;

import com.example.app.loyalty.domain.PointsTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointsTransactionRepository extends JpaRepository<PointsTransaction, Long> {
    Page<PointsTransaction> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}


