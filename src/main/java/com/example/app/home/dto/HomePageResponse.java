package com.example.app.home.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomePageResponse {
    private HeroSectionDto hero;
    private List<TestimonialDto> testimonials;
    // Service categories will be fetched from services module via application layer
}



