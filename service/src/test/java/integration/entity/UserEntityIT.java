package integration.entity;

import com.nikita.shop.entity.Activity;
import com.nikita.shop.entity.AddressEntity;
import com.nikita.shop.entity.OrderStatus;
import com.nikita.shop.entity.ProductReviewEntity;
import com.nikita.shop.entity.PromoCodeEntity;
import com.nikita.shop.entity.Role;
import com.nikita.shop.entity.ShoppingCartEntity;
import com.nikita.shop.entity.UserEntity;
import com.nikita.shop.entity.UserPromoCodeEntity;
import com.nikita.shop.entity.embeddable.AddressInfo;
import com.nikita.shop.entity.embeddable.ProductReviewInfo;
import com.nikita.shop.entity.embeddable.ShoppingCartDate;
import com.nikita.shop.entity.embeddable.UserActivity;
import com.nikita.shop.entity.embeddable.UserCredentials;
import com.nikita.shop.entity.embeddable.UserPersonalInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.TransactionManager;

import java.time.Instant;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UserEntityIT extends TransactionManager {

    @Test
    void save() {
        var userEntity = getUserEntity();

        session.persist(userEntity);
        session.flush();
        session.clear();

        var savedUser = session.get(UserEntity.class, userEntity.getId());
        Assertions.assertNotNull(savedUser);
    }

    @Test
    void addNewAddressInUser() {
        var userEntity = getUserEntity();
        var addressEntity = getAddressEntity();
        userEntity.addAddress(addressEntity);
        addressEntity.setUser(userEntity);

        session.persist(userEntity);
        session.flush();
        session.clear();

        var user = session.get(UserEntity.class, userEntity.getId());
        Assertions.assertNotNull(user);
        assertEquals(1, user.getAddresses().size());
    }

    @Test
    void addNewPromoCodeInUser() {
        var userEntity = getUserEntity();
        var promoCodeEntity = getPromoCodeEntity();
        var userPromoCodeEntity = getUserPromoCodeEntity();
        session.persist(userEntity);
        session.persist(promoCodeEntity);
        userPromoCodeEntity.setUser(userEntity);
        userPromoCodeEntity.setPromoCode(promoCodeEntity);

        session.persist(userPromoCodeEntity);
        session.flush();
        session.clear();

        var userPromoCode = session.get(UserPromoCodeEntity.class, userPromoCodeEntity.getId());
        Assertions.assertNotNull(userPromoCode);
        assertEquals(userEntity.getId(), userPromoCode.getUser().getId());
        assertEquals(promoCodeEntity.getId(), userPromoCode.getPromoCode().getId());
    }

    @Test
    void addNewShoppingCartInUser() {
        var userEntity = getUserEntity();
        var shoppingCartEntity = getShoppingCartEntity();

        shoppingCartEntity.setUser(userEntity);
        userEntity.addShoppingCart(shoppingCartEntity);

        session.persist(userEntity);
        session.flush();
        session.clear();

        var user = session.get(UserEntity.class, userEntity.getId());
        Assertions.assertNotNull(user);
        assertEquals(1, user.getShoppingCarts().size());
    }

    @Test
    void addNewProductReviewInUser() {
        var userEntity = getUserEntity();
        var productReviewEntity = getProductReviewEntity();

        userEntity.addProductReview(productReviewEntity);
        productReviewEntity.setUser(userEntity);

        session.persist(userEntity);
        session.flush();
        session.clear();

        var user = session.get(UserEntity.class, userEntity.getId());
        Assertions.assertNotNull(user);
        assertEquals(1, user.getProductReviews().size());
    }

    @Test
    void get() {
        var userEntity = getUserEntity();

        session.persist(userEntity);
        session.flush();
        session.clear();

        var userResult = session.get(UserEntity.class, userEntity.getId());
        Assertions.assertNotNull(userResult);
    }

    @Test
    void update() {
        var userEntity = getUserEntity();
        session.persist(userEntity);
        session.flush();
        session.clear();

        var user = session.get(UserEntity.class, userEntity.getId());
        user.setPhoneNumber("1005000");
        session.flush();
        session.clear();

        var userResult = session.get(UserEntity.class, user.getId());
        assertEquals("1005000", userResult.getPhoneNumber());
    }

    @Test
    void delete() {
        var userEntity = getUserEntity();
        session.persist(userEntity);
        session.flush();
        session.clear();

        var userToDelete = session.get(UserEntity.class, userEntity.getId());
        session.remove(userToDelete);
        session.flush();
        session.clear();

        assertNull(session.get(UserEntity.class, userEntity.getId()));
    }

    @Test
    void deleteUserAndAddresses() {
        var userEntity = getUserEntity();
        var addressEntity = getAddressEntity();
        userEntity.addAddress(addressEntity);
        addressEntity.setUser(userEntity);

        session.persist(userEntity);
        session.flush();
        session.clear();

        var userToDelete = session.get(UserEntity.class, userEntity.getId());
        session.remove(userToDelete);
        session.flush();
        session.clear();

        assertNull(session.get(UserEntity.class, userEntity.getId()));
        assertNull(session.get(AddressEntity.class, addressEntity.getId()));
    }

    @Test
    void deleteUserAndUserPromoCode() {
        var userEntity = getUserEntity();
        var userPromoCodeEntity = getUserPromoCodeEntity();

        userPromoCodeEntity.setUser(userEntity);

        session.persist(userEntity);
        session.flush();
        session.clear();

        var userToDelete = session.get(UserEntity.class, userEntity.getId());
        session.remove(userToDelete);
        session.flush();
        session.clear();

        assertNull(session.get(UserEntity.class, userEntity.getId()));
        assertNull(session.get(UserPromoCodeEntity.class, userPromoCodeEntity.getId()));
    }

    @Test
    void deleteUserAndShoppingCarts() {
        var userEntity = getUserEntity();
        var shoppingCartEntity = getShoppingCartEntity();

        shoppingCartEntity.setUser(userEntity);
        userEntity.addShoppingCart(shoppingCartEntity);

        session.persist(userEntity);
        session.flush();
        session.clear();

        var userToDelete = session.get(UserEntity.class, userEntity.getId());
        session.remove(userToDelete);
        session.flush();
        session.clear();

        assertNull(session.get(UserEntity.class, userEntity.getId()));
        assertNull(session.get(ShoppingCartEntity.class, shoppingCartEntity.getId()));
    }

    @Test
    void deleteUserAndProductReviews() {
        var userEntity = getUserEntity();
        var productReviewEntity = getProductReviewEntity();

        userEntity.addProductReview(productReviewEntity);
        productReviewEntity.setUser(userEntity);

        session.persist(userEntity);
        session.flush();
        session.clear();

        var userToDelete = session.get(UserEntity.class, userEntity.getId());
        session.remove(userToDelete);
        session.flush();
        session.clear();

        assertNull(session.get(UserEntity.class, userEntity.getId()));
        assertNull(session.get(ProductReviewEntity.class, productReviewEntity.getId()));
    }


    private UserEntity getUserEntity() {
        return UserEntity.builder()
                .role(Role.ADMIN)
                .phoneNumber("895200000")
                .personalInfo(UserPersonalInfo.builder()
                        .image("f")
                        .birthDate(LocalDate.of(2000, 10, 12))
                        .lastname("F")
                        .firstname("M")
                        .build())
                .credentials(UserCredentials.builder()
                        .password("12456")
                        .emailVerified(true)
                        .email("fafsa@mail.ru")
                        .build())
                .activity(UserActivity.builder()
                        .registrationDate(Instant.now())
                        .activity(Activity.ALLOWED)
                        .lastLogin(Instant.now())
                        .build())
                .build();
    }

    private PromoCodeEntity getPromoCodeEntity() {
        return PromoCodeEntity.builder()
                .code("1")
                .discountSum(10)
                .remainingQuantity(10)
                .activityPromo(true)
                .minOrderAmount(10)
                .validTo(Instant.now())
                .validFrom(Instant.now())
                .build();
    }

    private UserPromoCodeEntity getUserPromoCodeEntity() {
        return UserPromoCodeEntity.builder()
                .usedAt(Instant.now())
                .build();
    }

    private AddressEntity getAddressEntity() {
        return AddressEntity.builder()
                .addressInfo(AddressInfo.builder()
                        .country("Russia")
                        .city("London")
                        .street("fa")
                        .houseNumber(2)
                        .apartmentNumber(23)
                        .build())
                .build();
    }

    private ShoppingCartEntity getShoppingCartEntity() {
        return ShoppingCartEntity.builder()
                .shoppingCartDate(ShoppingCartDate.builder()
                        .creatTime(Instant.now())
                        .updateTime(Instant.now())
                        .build())
                .orderStatus(OrderStatus.ASSEMBLY)
                .build();
    }

    private ProductReviewEntity getProductReviewEntity() {
        return ProductReviewEntity.builder()
                .productReviewInfo(ProductReviewInfo.builder()
                        .createTime(Instant.now())
                        .description("faf")
                        .image("faf")
                        .rating(5)
                        .build())
                .build();
    }


}
