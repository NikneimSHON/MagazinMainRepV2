package com.nikita.shop.database.filter;

import com.nikita.shop.entity.embeddable.ProductCategory;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductFilter {
    String productName;
    ProductCategory category;
    Integer maxCount;
    Integer minCount;
    Integer minPrice;
    Integer maxPrice;
    Boolean available;
}
