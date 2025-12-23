package com.example.app.orders.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    @NotNull
    private Long serviceId;

    private Long workerId;

    private Instant scheduledAt;

    private Integer durationMinutes;

    @NotBlank
    private String addressLine;

    private String district;
    private String city;
    private String postalCode;
    private String country;

    private String notes;
}







