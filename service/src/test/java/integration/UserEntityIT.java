package integration;

import entity.*;
import entity.embeddable.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;
import util.HibernateTestUtil;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserEntityIT {

    private static SessionFactory sessionFactory;
    private Session session;
    private Transaction transaction;


    @BeforeAll
    static void openSessionFactory() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
    }

    @BeforeEach
    void openSessionAndTransaction() {
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
    }

    @Test
    void save() {
        var userEntity = getUserEntity();

        session.persist(userEntity);

        assertNotNull(userEntity.getId());

    }

    @Test
    void addNewAddressInUser() {
        var userEntity = getUserEntity();
        var addressEntity = getAddressEntity();
        userEntity.addAddress(addressEntity);
        addressEntity.setUser(userEntity);

        session.persist(addressEntity);
        session.persist(userEntity);

        var user = session.get(UserEntity.class, userEntity.getId());

        assertNotNull(user);
        assertTrue(user.getAddresses().contains(addressEntity));
    }

    @Test
    void addNewPromoCodeInUser() {
        var userEntity = getUserEntity();
        var promoCodeEntity = getPromoCodeEntity();
        var userPromoCodeEntity = getUserPromoCodeEntity();

        userPromoCodeEntity.setUser(userEntity);
        userPromoCodeEntity.setPromoCode(promoCodeEntity);

        session.persist(userPromoCodeEntity);
        session.persist(userEntity);
        session.persist(promoCodeEntity);

        var userPromoCode = session.get(UserPromoCodeEntity.class, userPromoCodeEntity.getId());
        assertNotNull(userPromoCode);
        assertEquals(userEntity.getId(), userPromoCode.getUser().getId());
        assertEquals(promoCodeEntity.getId(), userPromoCode.getPromoCode().getId());
    }

    @Test
    void addNewShoppingCartInUser() {
        var userEntity = getUserEntity();
        var shoppingCartEntity = getShoppingCartEntity();

        shoppingCartEntity.setUser(userEntity);
        userEntity.addShoppingCart(shoppingCartEntity);

        session.persist(shoppingCartEntity);
        session.persist(userEntity);

        var user = session.get(UserEntity.class, userEntity.getId());

        assertNotNull(user);
        assertTrue(user.getShoppingCarts().contains(shoppingCartEntity));

    }

    @Test
    void addNewProductReviewInUser() {
        var userEntity = getUserEntity();
        var productReviewEntity = getProductReviewEntity();

        userEntity.addProductReview(productReviewEntity);
        productReviewEntity.setUser(userEntity);

        session.persist(productReviewEntity);
        session.persist(userEntity);

        var user = session.get(UserEntity.class, userEntity.getId());

        assertNotNull(user);
        assertTrue(user.getProductReviews().contains(productReviewEntity));


    }

    @Test
    void get() {
        var userEntity = getUserEntity();

        session.persist(userEntity);
        var userResult = session.get(UserEntity.class, userEntity.getId());

        assertNotNull(userResult);
    }

    @Test
    void update() {
        var userEntity = getUserEntity();
        session.persist(userEntity);

        var user = session.get(UserEntity.class, userEntity.getId());
        user.setPhoneNumber("1005000");
        session.merge(user);

        var userResult = session.get(UserEntity.class, user.getId());
        assertEquals("1005000", userResult.getPhoneNumber());
    }


    @Test
    void delete() {
        var userEntity = getUserEntity();
        session.persist(userEntity);

        session.remove(userEntity);

        assertNull(session.get(UserEntity.class, userEntity.getId()));

    }

    @Test
    void deleteUserAndAddresses() {
        var userEntity = getUserEntity();
        var addressEntity = getAddressEntity();
        userEntity.addAddress(addressEntity);
        addressEntity.setUser(userEntity);

        session.persist(userEntity);
        session.persist(addressEntity);
        session.remove(userEntity);

        var deletedUser = session.get(UserEntity.class, userEntity.getId());
        var deletedAddress = session.get(AddressEntity.class, addressEntity.getId());

        assertNull(deletedUser);
        assertNull(deletedAddress);

    }

    @Test
    void deleteUserAndUserPromoCode() {
        var userEntity = getUserEntity();
        var userPromoCodeEntity = getUserPromoCodeEntity();

        userPromoCodeEntity.setUser(userEntity);

        session.persist(userEntity);
        session.persist(userPromoCodeEntity);

        session.remove(userEntity);

        var deletedUser = session.get(UserEntity.class, userEntity.getId());
        var deletedUserPromoCode = session.get(UserPromoCodeEntity.class, userPromoCodeEntity.getId());

        assertNull(deletedUser);
        assertNull(deletedUserPromoCode);

    }

    @Test
    void deleteUserAndShoppingCarts() {
        var userEntity = getUserEntity();
        var shoppingCartEntity = getShoppingCartEntity();

        shoppingCartEntity.setUser(userEntity);
        userEntity.addShoppingCart(shoppingCartEntity);

        session.persist(userEntity);
        session.persist(shoppingCartEntity);

        session.remove(userEntity);

        var deletedUser = session.get(UserEntity.class, userEntity.getId());
        var deletedShoppingCart = session.get(ShoppingCartEntity.class, shoppingCartEntity.getId());

        assertNull(deletedUser);
        assertNull(deletedShoppingCart);
    }

    @Test
    void deleteUserAndProductReviews() {
        var userEntity = getUserEntity();
        var productReviewEntity = getProductReviewEntity();

        userEntity.addProductReview(productReviewEntity);
        productReviewEntity.setUser(userEntity);

        session.persist(userEntity);
        session.persist(productReviewEntity);

        session.remove(userEntity);

        var deletedUser = session.get(UserEntity.class, userEntity.getId());
        var deletedProductReview = session.get(ProductReviewEntity.class, productReviewEntity.getId());

        assertNull(deletedUser);
        assertNull(deletedProductReview);

    }


    @AfterEach
    void closeSessionAndRollbackTransaction() {
        if (transaction != null && transaction.isActive()) {
            transaction.rollback();
        }

        if (session != null && session.isOpen()) {
            session.close();
        }
    }

    @AfterAll
    static void closeSessionFactory() {
        sessionFactory.close();
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
                        .activity(ActivityType.ALLOWED)
                        .lastLogin(Instant.now())
                        .build())
                .build();
    }

    private PromoCodeEntity getPromoCodeEntity() {
        return PromoCodeEntity.builder()
                .code("1")
                .discountSum(BigDecimal.valueOf(10))
                .remainingQuantity(10)
                .activityPromo(true)
                .minOrderAmount(BigDecimal.valueOf(10))
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
                        .creationTime(Instant.now())
                        .updateTime(Instant.now())
                        .build())
                .orderStatus("f")
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
