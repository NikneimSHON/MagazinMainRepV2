package com.nikita.shop.dto;

import com.nikita.shop.database.entity.embeddable.ProductCategory;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ProductFilter {
    String productName;
    String description;
    ProductCategory category;
    Integer maxCount;
    Integer minCount;
    Integer minPrice;
    Integer maxPrice;
    boolean available;
}
