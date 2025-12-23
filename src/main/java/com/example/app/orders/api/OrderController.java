package com.example.app.orders.api;

import com.example.app.orders.application.OrderService;
import com.example.app.orders.domain.Order;
import com.example.app.orders.dto.CreateReviewRequest;
import com.example.app.orders.dto.OrderDto;
import com.example.app.orders.dto.OrderReviewDto;
import com.example.app.shared.response.ApiResponse;
import com.example.app.shared.util.PageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Order management APIs")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    @Operation(summary = "Get user orders")
    public ResponseEntity<ApiResponse<PageUtil.PageResponse<OrderDto>>> getUserOrders(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Long userId = Long.parseLong(authentication.getName());
        PageUtil.PageResponse<OrderDto> response = orderService.getUserOrders(userId, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID")
    public ResponseEntity<ApiResponse<OrderDto>> getOrderById(
            Authentication authentication,
            @PathVariable Long id) {
        Long userId = Long.parseLong(authentication.getName());
        OrderDto response = orderService.getOrderById(id, userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping
    @Operation(summary = "Create a new order/booking")
    public ResponseEntity<ApiResponse<OrderDto>> createOrder(
            Authentication authentication,
            @Valid @RequestBody com.example.app.orders.dto.CreateOrderRequest request) {
        Long userId = Long.parseLong(authentication.getName());
        OrderDto response = orderService.createOrder(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response, "Order created"));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get orders by status")
    public ResponseEntity<ApiResponse<List<OrderDto>>> getOrdersByStatus(
            Authentication authentication,
            @PathVariable Order.OrderStatus status) {
        Long userId = Long.parseLong(authentication.getName());
        List<OrderDto> response = orderService.getUserOrdersByStatus(userId, status);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/{orderId}/reviews")
    @Operation(summary = "Create order review")
    public ResponseEntity<ApiResponse<OrderReviewDto>> createReview(
            Authentication authentication,
            @PathVariable Long orderId,
            @Valid @RequestBody CreateReviewRequest request) {
        Long userId = Long.parseLong(authentication.getName());
        OrderReviewDto response = orderService.createReview(orderId, userId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Review created successfully"));
    }

    @GetMapping("/service/{serviceId}/reviews")
    @Operation(summary = "Get reviews for a service")
    public ResponseEntity<ApiResponse<List<OrderReviewDto>>> getServiceReviews(@PathVariable Long serviceId) {
        List<OrderReviewDto> response = orderService.getServiceReviews(serviceId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}


