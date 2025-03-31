package com.nikita.shop.dto;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class ProductReviewFilter {
    Long userId;
    Integer rating;
    Instant beforeCreateTime;
    Instant afterCreateTime;
    String description;
}
