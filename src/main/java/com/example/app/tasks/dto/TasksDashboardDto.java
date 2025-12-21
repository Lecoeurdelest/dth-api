package com.example.app.tasks.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TasksDashboardDto {
    private ProfileSummaryDto profile;
    private OrdersSummaryDto orders;
    private PromotionsSummaryDto promotions;
}


