package entity;

import jakarta.persistence.*;
import lombok.*;


import java.time.Instant;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"user","promoCode"})
@Table(name = "user_promo_code")
public class UserPromoCodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserEntity user;

    @ManyToOne
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


}
