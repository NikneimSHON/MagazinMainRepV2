package com.nikita.shop.entity.embeddable;

import com.nikita.shop.entity.ActivityType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class UserActivity {
    private Instant lastLogin;
    private Instant registrationDate;
    @Enumerated(EnumType.STRING)
    private ActivityType activity;
}
