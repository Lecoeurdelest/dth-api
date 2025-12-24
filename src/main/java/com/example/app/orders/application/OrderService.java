package com.example.app.orders.application;

import com.example.app.orders.domain.Order;
import com.example.app.orders.domain.OrderReview;
import com.example.app.orders.dto.CreateReviewRequest;
import com.example.app.orders.dto.OrderDto;
import com.example.app.orders.dto.OrderReviewDto;
import com.example.app.orders.mapper.OrderMapper;
import com.example.app.orders.repository.OrderRepository;
import com.example.app.orders.repository.OrderReviewRepository;
import com.example.app.shared.exception.BadRequestException;
import com.example.app.shared.exception.ResourceNotFoundException;
import com.example.app.shared.util.PageUtil;
import com.example.app.workers.application.WorkerAvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderReviewRepository orderReviewRepository;
    private final OrderMapper orderMapper;
    private final WorkerAvailabilityService workerAvailabilityService;

    @Transactional(readOnly = true)
    public PageUtil.PageResponse<OrderDto> getUserOrders(Long userId, int page, int size, String sortBy, String sortDir) {
        Pageable pageable = PageUtil.createPageable(page, size, sortBy, sortDir);
        Page<Order> orders = orderRepository.findByUserId(userId, pageable);
        return PageUtil.toPageResponse(orders.map(orderMapper::toDto));
    }

    @Transactional(readOnly = true)
    public OrderDto getOrderById(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        if (!order.getUserId().equals(userId)) {
            throw new ResourceNotFoundException("Order not found");
        }

        return orderMapper.toDto(order);
    }

    @Transactional(readOnly = true)
    public List<OrderDto> getUserOrdersByStatus(Long userId, Order.OrderStatus status) {
        return orderRepository.findByUserIdAndStatus(userId, status).stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderReviewDto createReview(Long orderId, Long userId, CreateReviewRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        if (!order.getUserId().equals(userId)) {
            throw new BadRequestException("You can only review your own orders");
        }

        if (orderReviewRepository.findByOrderId(orderId).isPresent()) {
            throw new BadRequestException("Review already exists for this order");
        }

        OrderReview review = OrderReview.builder()
                .orderId(orderId)
                .userId(userId)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        review = orderReviewRepository.save(review);
        return orderMapper.toReviewDto(review);
    }

    @Transactional
    public OrderDto createOrder(Long userId, com.example.app.orders.dto.CreateOrderRequest request) {
        // Validate worker availability if worker is specified
        if (request.getWorkerId() != null) {
            boolean isAvailable = workerAvailabilityService.isWorkerAvailable(
                    request.getWorkerId(),
                    request.getScheduledAt(),
                    request.getDurationMinutes()
            );

            if (!isAvailable) {
                throw new BadRequestException("Thợ này không khả dụng trong khung giờ đã chọn. Vui lòng chọn thợ khác hoặc thời gian khác.");
            }
        }

        Order order = Order.builder()
                .userId(userId)
                .serviceId(request.getServiceId())
                .workerId(request.getWorkerId())
                .scheduledAt(request.getScheduledAt())
                .durationMinutes(request.getDurationMinutes())
                .status(Order.OrderStatus.PENDING)
                .totalAmount(java.math.BigDecimal.ZERO)
                .notes(request.getNotes())
                .build();

        order = orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Transactional(readOnly = true)
    public List<OrderReviewDto> getServiceReviews(Long serviceId) {
        return orderReviewRepository.findByServiceId(serviceId).stream()
                .map(orderMapper::toReviewDto)
                .collect(Collectors.toList());
    }
}


