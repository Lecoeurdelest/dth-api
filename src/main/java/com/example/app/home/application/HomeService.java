package com.example.app.home.application;

import com.example.app.home.dto.HeroSectionDto;
import com.example.app.home.dto.HomePageResponse;
import com.example.app.home.dto.TestimonialDto;
import com.example.app.home.mapper.HomeMapper;
import com.example.app.home.repository.HomeDataRepository;
import com.example.app.home.repository.TestimonialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final HomeDataRepository homeDataRepository;
    private final TestimonialRepository testimonialRepository;
    private final HomeMapper homeMapper;

    @Transactional(readOnly = true)
    public HomePageResponse getHomePageData() {
        // Get hero section data (using first record or default)
        var heroSection = homeDataRepository.findAll().stream()
                .findFirst()
                .map(homeMapper::toHeroDto)
                .orElse(HeroSectionDto.builder()
                        .title("Welcome")
                        .description("Default description")
                        .build());

        // Get featured testimonials
        List<TestimonialDto> testimonials = testimonialRepository.findByFeaturedTrue().stream()
                .map(homeMapper::toTestimonialDto)
                .collect(Collectors.toList());

        return HomePageResponse.builder()
                .hero(heroSection)
                .testimonials(testimonials)
                .build();
    }
}

