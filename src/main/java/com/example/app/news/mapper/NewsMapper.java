package com.example.app.news.mapper;

import com.example.app.news.domain.News;
import com.example.app.news.dto.NewsDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NewsMapper {
    NewsDto toDto(News news);
}


