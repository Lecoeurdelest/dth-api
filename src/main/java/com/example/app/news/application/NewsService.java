package com.example.app.news.application;

import com.example.app.news.domain.News;
import com.example.app.news.dto.NewsDto;
import com.example.app.news.mapper.NewsMapper;
import com.example.app.news.repository.NewsRepository;
import com.example.app.shared.exception.ResourceNotFoundException;
import com.example.app.shared.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;

    @Transactional(readOnly = true)
    public PageUtil.PageResponse<NewsDto> getAllNews(int page, int size, String sortBy, String sortDir) {
        Pageable pageable = PageUtil.createPageable(page, size, sortBy, sortDir);
        Page<News> news = newsRepository.findByPublishedTrue(pageable);
        return PageUtil.toPageResponse(news.map(newsMapper::toDto));
    }

    @Transactional(readOnly = true)
    public NewsDto getNewsById(Long id) {
        return newsRepository.findById(id)
                .filter(n -> n.getPublished())
                .map(newsMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("News not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<NewsDto> getFeaturedNews() {
        return newsRepository.findByFeaturedTrue().stream()
                .map(newsMapper::toDto)
                .collect(Collectors.toList());
    }
}

