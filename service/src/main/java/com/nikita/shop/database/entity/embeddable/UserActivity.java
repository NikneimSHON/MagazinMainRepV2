package com.nikita.shop.database.entity.embeddable;

import com.nikita.shop.database.entity.Activity;
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
    private Activity activity;
}
