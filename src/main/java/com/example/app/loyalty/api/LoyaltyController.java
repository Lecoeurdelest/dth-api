package com.example.app.loyalty.api;

import com.example.app.loyalty.application.LoyaltyService;
import com.example.app.loyalty.dto.LoyaltyPointsDto;
import com.example.app.loyalty.dto.PointsTransactionDto;
import com.example.app.shared.response.ApiResponse;
import com.example.app.shared.util.PageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loyalty")
@RequiredArgsConstructor
@Tag(name = "Loyalty", description = "Loyalty points APIs")
@SecurityRequirement(name = "bearerAuth")
public class LoyaltyController {

    private final LoyaltyService loyaltyService;

    @GetMapping("/points")
    @Operation(summary = "Get user loyalty points")
    public ResponseEntity<ApiResponse<LoyaltyPointsDto>> getLoyaltyPoints(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        LoyaltyPointsDto response = loyaltyService.getLoyaltyPoints(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/history")
    @Operation(summary = "Get points transaction history")
    public ResponseEntity<ApiResponse<PageUtil.PageResponse<PointsTransactionDto>>> getPointsHistory(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Long userId = Long.parseLong(authentication.getName());
        PageUtil.PageResponse<PointsTransactionDto> response = loyaltyService.getPointsHistory(userId, page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}


