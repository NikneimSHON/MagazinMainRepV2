package com.nikita.shop.database.entity.embeddable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class ProductInfo {
    private String name;
    private Integer price;
    private String description;
    @Enumerated(EnumType.STRING)
    private ProductCategory category;
    private Integer count;
    private String productImage;
}
