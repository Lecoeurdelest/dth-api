package com.example.app.tasks.api;

import com.example.app.tasks.application.TasksService;
import com.example.app.tasks.dto.TasksDashboardDto;
import com.example.app.shared.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Tasks dashboard APIs")
@SecurityRequirement(name = "bearerAuth")
public class TasksController {

    private final TasksService tasksService;

    @GetMapping("/dashboard")
    @Operation(summary = "Get tasks dashboard data")
    public ResponseEntity<ApiResponse<TasksDashboardDto>> getDashboard(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        TasksDashboardDto response = tasksService.getDashboard(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}


