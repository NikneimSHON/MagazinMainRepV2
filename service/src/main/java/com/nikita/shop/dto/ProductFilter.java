package com.nikita.shop.dto;

import com.nikita.shop.entity.embeddable.ProductCategory;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@Builder
public class ProductFilter {
    String productName;
    String description;
    ProductCategory category;
    int maxCount;
    int minCount;
    int minPrice;
    int maxPrice;
    boolean available;
}
