package com.nikita.shop.database.filter;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
@Data
@Builder
public class PromoCodeFilter {
    Boolean activityPromo;
    Integer minDiscountSum;
    Integer maxDiscountSum;
    Integer minOrderAmount;
    Instant validFrom;
    Instant validTo;

}
