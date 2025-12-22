package com.example.app.loyalty.application;

import com.example.app.loyalty.domain.LoyaltyPoints;
import com.example.app.loyalty.domain.PointsTransaction;
import com.example.app.loyalty.dto.LoyaltyPointsDto;
import com.example.app.loyalty.dto.PointsTransactionDto;
import com.example.app.loyalty.mapper.LoyaltyMapper;
import com.example.app.loyalty.repository.LoyaltyPointsRepository;
import com.example.app.loyalty.repository.PointsTransactionRepository;
import com.example.app.shared.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoyaltyService {

    private final LoyaltyPointsRepository loyaltyPointsRepository;
    private final PointsTransactionRepository pointsTransactionRepository;
    private final LoyaltyMapper loyaltyMapper;

    @Transactional(readOnly = true)
    public LoyaltyPointsDto getLoyaltyPoints(Long userId) {
        LoyaltyPoints loyaltyPoints = loyaltyPointsRepository.findByUserId(userId)
                .orElseGet(() -> LoyaltyPoints.builder()
                        .userId(userId)
                        .pointsBalance(0)
                        .currentTier(LoyaltyPoints.Tier.BRONZE)
                        .build());

        LoyaltyPointsDto dto = loyaltyMapper.toDto(loyaltyPoints);
        dto.setPointsToNextTier(calculatePointsToNextTier(loyaltyPoints.getCurrentTier(), loyaltyPoints.getPointsBalance()));
        return dto;
    }

    @Transactional(readOnly = true)
    public PageUtil.PageResponse<PointsTransactionDto> getPointsHistory(Long userId, int page, int size) {
        Pageable pageable = PageUtil.createPageable(page, size, "createdAt", "desc");
        Page<PointsTransaction> transactions = pointsTransactionRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        return PageUtil.toPageResponse(transactions.map(loyaltyMapper::toTransactionDto));
    }

    private Integer calculatePointsToNextTier(LoyaltyPoints.Tier currentTier, Integer currentPoints) {
        // Simple tier calculation logic
        int[] tierThresholds = {0, 100, 500, 1000}; // BRONZE, SILVER, GOLD, PLATINUM
        int tierIndex = currentTier.ordinal();
        
        if (tierIndex >= tierThresholds.length - 1) {
            return 0; // Already at highest tier
        }
        
        int nextTierThreshold = tierThresholds[tierIndex + 1];
        return Math.max(0, nextTierThreshold - currentPoints);
    }

    @Transactional
    public LoyaltyPoints.Tier calculateTier(Integer points) {
        if (points >= 1000) return LoyaltyPoints.Tier.PLATINUM;
        if (points >= 500) return LoyaltyPoints.Tier.GOLD;
        if (points >= 100) return LoyaltyPoints.Tier.SILVER;
        return LoyaltyPoints.Tier.BRONZE;
    }
}


