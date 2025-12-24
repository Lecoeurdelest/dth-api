package com.example.app.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminStatsDto {
    private Long totalUsers;
    private Long totalOrders;
    private Long totalWorkers;
    private Long totalServices;
    private Long pendingOrders;
    private Long inProgressOrders;
    private Long completedOrders;
    private Long cancelledOrders;
}
