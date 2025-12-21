package com.example.app.loyalty.dto;

import com.example.app.loyalty.domain.LoyaltyPoints;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoyaltyPointsDto {
    private Long id;
    private Long userId;
    private Integer pointsBalance;
    private LoyaltyPoints.Tier currentTier;
    private Integer pointsToNextTier;
}


