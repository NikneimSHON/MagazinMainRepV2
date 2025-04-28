package com.nikita.shop.database.filter;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ProductReviewFilter {
    Long userId;
    Integer rating;
    Instant beforeCreateTime;
    Instant afterCreateTime;
}
