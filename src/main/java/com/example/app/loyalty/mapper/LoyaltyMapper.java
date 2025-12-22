package com.example.app.loyalty.mapper;

import com.example.app.loyalty.domain.LoyaltyPoints;
import com.example.app.loyalty.domain.PointsTransaction;
import com.example.app.loyalty.dto.LoyaltyPointsDto;
import com.example.app.loyalty.dto.PointsTransactionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LoyaltyMapper {
    @Mapping(source = "pointsBalance", target = "pointsBalance")
    @Mapping(target = "pointsToNextTier", ignore = true) // Computed field, set in service
    LoyaltyPointsDto toDto(LoyaltyPoints loyaltyPoints);
    
    PointsTransactionDto toTransactionDto(PointsTransaction transaction);
}


