package com.example.app.home.mapper;

import com.example.app.home.domain.HomeData;
import com.example.app.home.domain.Testimonial;
import com.example.app.home.dto.HeroSectionDto;
import com.example.app.home.dto.TestimonialDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HomeMapper {
    @Mapping(source = "heroTitle", target = "title")
    @Mapping(source = "heroDescription", target = "description")
    @Mapping(source = "heroImageUrl", target = "imageUrl")
    HeroSectionDto toHeroDto(HomeData homeData);
    
    TestimonialDto toTestimonialDto(Testimonial testimonial);
}








