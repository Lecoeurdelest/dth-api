package com.example.app.admin.api;

import com.example.app.admin.application.AdminService;
import com.example.app.admin.dto.AdminStatsDto;
import com.example.app.admin.dto.OrderManagementDto;
import com.example.app.admin.dto.UserManagementDto;
import com.example.app.shared.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dashboard/stats")
    public ResponseEntity<ApiResponse<AdminStatsDto>> getDashboardStats() {
        AdminStatsDto stats = adminService.getDashboardStats();
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<Page<UserManagementDto>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("ASC") 
                ? Sort.by(sortBy).ascending() 
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UserManagementDto> users = adminService.getAllUsers(pageable);
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse<UserManagementDto>> getUserById(@PathVariable Long id) {
        UserManagementDto user = adminService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @PutMapping("/users/{id}/block")
    public ResponseEntity<ApiResponse<UserManagementDto>> blockUser(@PathVariable Long id) {
        UserManagementDto user = adminService.blockUser(id);
        return ResponseEntity.ok(ApiResponse.success(user, "User blocked successfully"));
    }

    @PutMapping("/users/{id}/unblock")
    public ResponseEntity<ApiResponse<UserManagementDto>> unblockUser(@PathVariable Long id) {
        UserManagementDto user = adminService.unblockUser(id);
        return ResponseEntity.ok(ApiResponse.success(user, "User unblocked successfully"));
    }

    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<Page<OrderManagementDto>>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("ASC") 
                ? Sort.by(sortBy).ascending() 
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<OrderManagementDto> orders = adminService.getAllOrders(pageable);
        return ResponseEntity.ok(ApiResponse.success(orders));
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<ApiResponse<OrderManagementDto>> getOrderById(@PathVariable Long id) {
        OrderManagementDto order = adminService.getOrderById(id);
        return ResponseEntity.ok(ApiResponse.success(order));
    }
}
