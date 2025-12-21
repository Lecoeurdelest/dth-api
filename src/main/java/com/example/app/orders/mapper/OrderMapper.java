package com.example.app.orders.mapper;

import com.example.app.orders.domain.Order;
import com.example.app.orders.domain.OrderReview;
import com.example.app.orders.dto.OrderDto;
import com.example.app.orders.dto.OrderReviewDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toDto(Order order);
    OrderReviewDto toReviewDto(OrderReview review);
}


