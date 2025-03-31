package com.nikita.shop.database.entity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EqualsAndHashCode(exclude = "userPromoCode")
@Table(name = "promo_code")
public class PromoCodeEntity implements BaseEntity<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int remainingQuantity;
    private String code;
    private boolean activityPromo;
    private Integer discountSum;
    private Integer minOrderAmount;
    private Instant validFrom;
    private Instant validTo;

    @Builder.Default
    @OneToMany(mappedBy = "promoCode", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserPromoCodeEntity> userPromoCode = new ArrayList<>();

    @Override
    public String toString() {
        return "PromoCodeEntity{" +
               "id=" + id +
               ", code='" + code + '\'' +
               ", userPromoCodeIds=" + (userPromoCode != null ? userPromoCode.stream()
                .map(UserPromoCodeEntity::getId)
                .toList() : "null") +
               '}';
    }

}
