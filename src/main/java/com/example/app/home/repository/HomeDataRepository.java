package com.example.app.home.repository;

import com.example.app.home.domain.HomeData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeDataRepository extends JpaRepository<HomeData, Long> {
}


