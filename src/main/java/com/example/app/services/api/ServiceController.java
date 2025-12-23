package com.example.app.services.api;

import com.example.app.services.application.ServiceApplicationService;
import com.example.app.services.dto.ServiceDto;
import com.example.app.shared.response.ApiResponse;
import com.example.app.shared.util.PageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/services")
@RequiredArgsConstructor
@Tag(name = "Services", description = "Service management API")
public class ServiceController {

    private final ServiceApplicationService serviceApplicationService;

    @GetMapping
    @Operation(summary = "Get all services")
    public ResponseEntity<ApiResponse<PageUtil.PageResponse<ServiceDto>>> getAllServices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        PageUtil.PageResponse<ServiceDto> response = serviceApplicationService.getAllServices(page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get service by ID")
    public ResponseEntity<ApiResponse<ServiceDto>> getServiceById(@PathVariable Long id) {
        ServiceDto response = serviceApplicationService.getServiceById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get services by category")
    public ResponseEntity<ApiResponse<List<ServiceDto>>> getServicesByCategory(@PathVariable String category) {
        List<ServiceDto> response = serviceApplicationService.getServicesByCategory(category);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}








