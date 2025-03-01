package com.nikita.shop.entity;

import com.nikita.shop.entity.embeddable.UserActivity;
import com.nikita.shop.entity.embeddable.UserCredentials;
import com.nikita.shop.entity.embeddable.UserPersonalInfo;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
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


    @Override
    public String toString() {
        return "UserEntity{" +
               "id=" + id +
               ", phoneNumber='" + phoneNumber + '\'' +
               ", role=" + role +
               ", personalInfo=" + personalInfo +
               ", credentials=" + credentials +
               ", activity=" + activity +
               ", addressesIds=" + (addresses != null ? addresses.stream().map(AddressEntity::getId).toList() : "null") +
               ", userPromoCodeIds=" + (userPromoCode != null ? userPromoCode.stream().map(UserPromoCodeEntity::getId).toList() : "null") +
               ", shoppingCartsIds=" + (shoppingCarts != null ? shoppingCarts.stream().map(ShoppingCartEntity::getId).toList() : "null") +
               ", productReviewsIds=" + (productReviews != null ? productReviews.stream().map(ProductReviewEntity::getId).toList() : "null") +
               '}';
    }


}
