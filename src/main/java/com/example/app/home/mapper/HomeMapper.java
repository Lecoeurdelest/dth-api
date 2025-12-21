package com.example.app.home.mapper;

import com.example.app.home.domain.HomeData;
import com.example.app.home.domain.Testimonial;
import com.example.app.home.dto.HeroSectionDto;
import com.example.app.home.dto.TestimonialDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HomeMapper {
    HeroSectionDto toHeroDto(HomeData homeData);
    TestimonialDto toTestimonialDto(Testimonial testimonial);
}


