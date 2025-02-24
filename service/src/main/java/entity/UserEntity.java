package entity;

import entity.embeddable.UserActivity;
import entity.embeddable.UserCredentials;
import entity.embeddable.UserPersonalInfo;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString(exclude = {"addresses", "userPromoCode", "shoppingCarts", "productReviews"})
@EqualsAndHashCode(exclude = {"addresses", "userPromoCode", "shoppingCarts", "productReviews"})
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Embedded
    private UserPersonalInfo personalInfo;
    @Embedded
    private UserCredentials credentials;
    @Embedded
    private UserActivity activity;

    @Builder.Default
    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<AddressEntity> addresses = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<UserPromoCodeEntity> userPromoCode = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<ShoppingCartEntity> shoppingCarts = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<ProductReviewEntity> productReviews = new ArrayList<>();


    public void addAddress(AddressEntity address) {
        addresses.add(address);
        address.setUser(this);
    }

    public void addShoppingCart(ShoppingCartEntity shoppingCart) {
        shoppingCarts.add(shoppingCart);
        shoppingCart.setUser(this);
    }

    public void addProductReview(ProductReviewEntity productReview) {
        productReviews.add(productReview);
        productReview.setUser(this);
    }


}
