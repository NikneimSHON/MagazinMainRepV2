package com.nikita.shop.entity.embeddable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class ProductInfo {
    private BigDecimal price;
    private String description;
    @Enumerated(EnumType.STRING)
    private ProductCategory category;
    private int count;
    private String productImage;
}
