package com.example.app.loyalty.dto;

import com.example.app.loyalty.domain.PointsTransaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointsTransactionDto {
    private Long id;
    private Integer points;
    private PointsTransaction.TransactionType type;
    private String description;
    private LocalDateTime createdAt;
}


