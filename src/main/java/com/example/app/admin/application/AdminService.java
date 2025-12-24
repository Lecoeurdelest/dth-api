package com.example.app.admin.application;

import com.example.app.admin.dto.AdminStatsDto;
import com.example.app.admin.dto.OrderManagementDto;
import com.example.app.admin.dto.UserManagementDto;
import com.example.app.auth.domain.User;
import com.example.app.auth.repository.UserRepository;
import com.example.app.orders.domain.Order;
import com.example.app.orders.repository.OrderRepository;
import com.example.app.services.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ServiceRepository serviceRepository;

    @Transactional(readOnly = true)
    public AdminStatsDto getDashboardStats() {
        // Count users by role (exclude ADMIN)
        long totalUsers = userRepository.countByRole(com.example.app.auth.domain.Role.USER);
        long totalWorkers = userRepository.countByRole(com.example.app.auth.domain.Role.WORKER);
        
        long totalOrders = orderRepository.count();
        long totalServices = serviceRepository.count();
        
        long pendingOrders = orderRepository.countByStatus(Order.OrderStatus.PENDING);
        long inProgressOrders = orderRepository.countByStatus(Order.OrderStatus.IN_PROGRESS);
        long completedOrders = orderRepository.countByStatus(Order.OrderStatus.COMPLETED);
        long cancelledOrders = orderRepository.countByStatus(Order.OrderStatus.CANCELLED);

        return AdminStatsDto.builder()
                .totalUsers(totalUsers)
                .totalOrders(totalOrders)
                .totalWorkers(totalWorkers)
                .totalServices(totalServices)
                .pendingOrders(pendingOrders)
                .inProgressOrders(inProgressOrders)
                .completedOrders(completedOrders)
                .cancelledOrders(cancelledOrders)
                .build();
    }

    @Transactional(readOnly = true)
    public Page<UserManagementDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(this::mapToUserManagementDto);
    }

    @Transactional(readOnly = true)
    public UserManagementDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToUserManagementDto(user);
    }

    @Transactional
    public UserManagementDto blockUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setAccountNonLocked(false);
        user = userRepository.save(user);
        return mapToUserManagementDto(user);
    }

    @Transactional
    public UserManagementDto unblockUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setAccountNonLocked(true);
        user = userRepository.save(user);
        return mapToUserManagementDto(user);
    }

    @Transactional(readOnly = true)
    public Page<OrderManagementDto> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(this::mapToOrderManagementDto);
    }

    @Transactional(readOnly = true)
    public OrderManagementDto getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return mapToOrderManagementDto(order);
    }

    @Transactional
    public OrderManagementDto updateOrderStatus(Long id, Order.OrderStatus newStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(newStatus);
        order = orderRepository.save(order);
        return mapToOrderManagementDto(order);
    }

    @Transactional
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Order not found");
        }
        orderRepository.deleteById(id);
    }

    @Transactional
    public OrderManagementDto createOrder(Long userId, Long serviceId, String notes) {
        // Validate user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Validate service exists
        com.example.app.services.domain.Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        // Create new order
        Order order = new Order();
        order.setUserId(userId);
        order.setServiceId(serviceId);
        order.setStatus(Order.OrderStatus.PENDING);
        order.setTotalAmount(service.getBasePrice());
        order.setNotes(notes);
        
        order = orderRepository.save(order);
        return mapToOrderManagementDto(order);
    }

    @Transactional
    public void createService(String name, String description, Double basePrice, String category, Boolean active) {
        com.example.app.services.domain.Service service = new com.example.app.services.domain.Service();
        service.setName(name);
        service.setDescription(description);
        service.setBasePrice(java.math.BigDecimal.valueOf(basePrice));
        service.setCategory(category);
        service.setActive(active != null ? active : true);
        
        serviceRepository.save(service);
    }

    @Transactional
    public void updateService(Long id, String name, String description, Double basePrice, String category, Boolean active) {
        com.example.app.services.domain.Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));
        
        service.setName(name);
        service.setDescription(description);
        service.setBasePrice(java.math.BigDecimal.valueOf(basePrice));
        service.setCategory(category);
        if (active != null) {
            service.setActive(active);
        }
        
        serviceRepository.save(service);
    }

    @Transactional
    public void deleteService(Long id) {
        if (!serviceRepository.existsById(id)) {
            throw new RuntimeException("Service not found");
        }
        serviceRepository.deleteById(id);
    }

    @Transactional
    public void toggleServiceActive(Long id) {
        com.example.app.services.domain.Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));
        service.setActive(!service.getActive());
        serviceRepository.save(service);
    }

    private UserManagementDto mapToUserManagementDto(User user) {
        return UserManagementDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .avatarUrl(user.getAvatarUrl())
                .role(user.getRole().name())
                .enabled(user.getEnabled())
                .accountNonLocked(user.getAccountNonLocked())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    private OrderManagementDto mapToOrderManagementDto(Order order) {
        // Fetch user and service info
        User user = userRepository.findById(order.getUserId()).orElse(null);
        com.example.app.services.domain.Service service = serviceRepository.findById(order.getServiceId()).orElse(null);

        return OrderManagementDto.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .customerName(user != null ? user.getFirstName() + " " + user.getLastName() : "Unknown")
                .customerEmail(user != null ? user.getEmail() : "")
                .serviceId(order.getServiceId())
                .serviceName(service != null ? service.getName() : "Unknown")
                .status(order.getStatus().name())
                .totalAmount(order.getTotalAmount())
                .notes(order.getNotes())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }
}
