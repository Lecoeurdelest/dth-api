package com.example.app.news.api;

import com.example.app.news.application.NewsService;
import com.example.app.news.dto.NewsDto;
import com.example.app.shared.response.ApiResponse;
import com.example.app.shared.util.PageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
@Tag(name = "News", description = "News APIs")
public class NewsController {

    private final NewsService newsService;

    @GetMapping
    @Operation(summary = "Get all news articles")
    public ResponseEntity<ApiResponse<PageUtil.PageResponse<NewsDto>>> getAllNews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        PageUtil.PageResponse<NewsDto> response = newsService.getAllNews(page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get news article by ID")
    public ResponseEntity<ApiResponse<NewsDto>> getNewsById(@PathVariable Long id) {
        NewsDto response = newsService.getNewsById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/featured")
    @Operation(summary = "Get featured news articles")
    public ResponseEntity<ApiResponse<List<NewsDto>>> getFeaturedNews() {
        List<NewsDto> response = newsService.getFeaturedNews();
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}


