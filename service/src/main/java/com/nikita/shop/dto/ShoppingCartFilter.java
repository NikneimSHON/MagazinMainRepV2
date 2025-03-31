package com.nikita.shop.dto;

import com.nikita.shop.database.entity.OrderStatus;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
@Value
@Builder
public class ShoppingCartFilter {
    OrderStatus status;
    Long userId;
    Instant beforeCreateTime;
    Instant afterCreateTime;
}
