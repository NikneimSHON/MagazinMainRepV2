package entity.embeddable;

import entity.ActivityType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class UserActivity {
    private Instant lastLogin;
    private Instant registrationDate;
    @Enumerated(EnumType.STRING)
    private ActivityType activity;
}
