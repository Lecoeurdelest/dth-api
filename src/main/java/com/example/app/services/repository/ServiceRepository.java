package com.example.app.services.repository;

import com.example.app.services.domain.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    Page<Service> findByActiveTrue(Pageable pageable);
    List<Service> findByCategory(String category);
    Optional<Service> findByIdAndActiveTrue(Long id);
}

