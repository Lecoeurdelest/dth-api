package com.example.app.home.api;

import com.example.app.home.application.HomeService;
import com.example.app.home.dto.HomePageResponse;
import com.example.app.shared.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
@Tag(name = "Home", description = "Home page APIs")
public class HomeController {

    private final HomeService homeService;

    @GetMapping
    @Operation(summary = "Get home page data")
    public ResponseEntity<ApiResponse<HomePageResponse>> getHomePage() {
        HomePageResponse response = homeService.getHomePageData();
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}








