package com.nikita.shop.database.filter;

import com.nikita.shop.entity.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
@Data
@Builder
public class ShoppingCartFilter {
    OrderStatus status;
    Long userId;
    Instant beforeCreateTime;
    Instant afterCreateTime;
}
