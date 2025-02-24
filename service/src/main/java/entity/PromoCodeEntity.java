package entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString(exclude = "UserPromoCode")
@Table(name = "promo_code")
public class PromoCodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int remainingQuantity;
    private String code;
    private boolean activityPromo;
    private BigDecimal discountSum;
    private BigDecimal minOrderAmount;
    private Instant validFrom;
    private Instant validTo;

    @Builder.Default
    @OneToMany(mappedBy = "promoCode", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserPromoCodeEntity> UserPromoCode = new ArrayList<>();


}
