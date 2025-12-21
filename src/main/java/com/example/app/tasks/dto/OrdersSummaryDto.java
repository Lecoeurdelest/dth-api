package com.example.app.tasks.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdersSummaryDto {
    private Long totalOrders;
    private Long pendingOrders;
    private Long completedOrders;
    private List<OrderSummaryItemDto> recentOrders;
}


