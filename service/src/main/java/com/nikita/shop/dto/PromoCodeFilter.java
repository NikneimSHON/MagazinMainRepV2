package com.nikita.shop.dto;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;
@Value
@Builder
public class PromoCodeFilter {
    boolean activityPromo;
    Integer minDiscountSum;
    Integer maxDiscountSum;
    Integer minOrderAmount;
    Instant validFrom;
    Instant validTo;

}
