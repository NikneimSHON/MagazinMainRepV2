package com.nikita.shop.database.entity.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class ShoppingCartDate {
    @Column(name = "create_time")
    private Instant creatTime;

    @Column(name = "update_time")
    private Instant updateTime;
}
