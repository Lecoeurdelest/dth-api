package com.example.app.orders.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderReviewDto {
    private Long id;
    private Long orderId;
    private Long userId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}


