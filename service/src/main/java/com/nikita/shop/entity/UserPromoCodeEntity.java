package com.nikita.shop.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_promo_code")
public class UserPromoCodeEntity implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    private PromoCodeEntity promoCode;

    private Instant usedAt;

    public void setUser(UserEntity user) {
        this.user = user;
        this.user.getUserPromoCode().add(this);
    }

    public void setPromoCode(PromoCodeEntity promoCode) {
        this.promoCode = promoCode;
        this.promoCode.getUserPromoCode().add(this);
    }

    @Override
    public String toString() {
        return "UserPromoCodeEntity{" +
               "id=" + id +
               ", userId=" + (user != null ? user.getId() : "null") +
               ", promoCodeId=" + (promoCode != null ? promoCode.getId() : "null") +
               ", usedAt=" + usedAt +
               '}';
    }
}
