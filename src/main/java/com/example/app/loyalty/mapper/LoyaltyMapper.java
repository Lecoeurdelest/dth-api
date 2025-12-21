package com.example.app.loyalty.mapper;

import com.example.app.loyalty.domain.LoyaltyPoints;
import com.example.app.loyalty.domain.PointsTransaction;
import com.example.app.loyalty.dto.LoyaltyPointsDto;
import com.example.app.loyalty.dto.PointsTransactionDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoyaltyMapper {
    LoyaltyPointsDto toDto(LoyaltyPoints loyaltyPoints);
    PointsTransactionDto toTransactionDto(PointsTransaction transaction);
}


