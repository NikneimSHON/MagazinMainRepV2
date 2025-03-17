package com.nikita.shop.dto;

import com.nikita.shop.entity.UserEntity;
import com.nikita.shop.entity.embeddable.ProductCategory;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class ProductReviewFilter {
    ProductFilter productFilter;
    UserEntity user;
    Integer rating;
    Instant beforeCreateTime;
    Instant afterCreateTime;
    String description;
}
