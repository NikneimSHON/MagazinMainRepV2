package integration;

import com.nikita.shop.entity.ActivityType;
import com.nikita.shop.entity.ProductReviewEntity;
import com.nikita.shop.entity.Role;
import com.nikita.shop.entity.UserEntity;
import com.nikita.shop.entity.embeddable.ProductReviewInfo;
import com.nikita.shop.entity.embeddable.UserActivity;
import com.nikita.shop.entity.embeddable.UserCredentials;
import com.nikita.shop.entity.embeddable.UserPersonalInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.TransactionManager;

import java.time.Instant;
import java.time.LocalDate;

public class ProductReviewEntityIT extends TransactionManager {

    @Test
    void save() {
        var productReviewEntity = getProductReviewEntity();

        session.persist(productReviewEntity);
        session.flush();
        session.clear();

        Assertions.assertNotNull(productReviewEntity.getId());
    }

    @Test
    void get() {
        var productReviewEntity = getProductReviewEntity();

        session.persist(productReviewEntity);
        session.flush();
        session.clear();

        var productReview = session.get(ProductReviewEntity.class, productReviewEntity.getId());
        Assertions.assertNotNull(productReview);
    }

    @Test
    void delete() {
        var productReviewEntity = getProductReviewEntity();

        session.persist(productReviewEntity);
        session.flush();
        session.clear();
        session.remove(productReviewEntity);
        session.flush();
        session.clear();

        Assertions.assertNull(session.get(ProductReviewEntity.class, productReviewEntity.getId()));
    }

    @Test
    void update() {
        var productReviewEntity = getProductReviewEntity();
        session.persist(productReviewEntity);
        session.flush();
        session.clear();

        var productReview = session.get(ProductReviewEntity.class, productReviewEntity.getId());
        productReview.getProductReviewInfo().setImage("fake");
        session.merge(productReview);
        session.flush();
        session.clear();

        Assertions.assertEquals("fake", productReview.getProductReviewInfo().getImage());
    }


    private ProductReviewEntity getProductReviewEntity() {
        return ProductReviewEntity.builder()
                .productReviewInfo(ProductReviewInfo.builder()
                        .rating(5)
                        .image("f")
                        .description("f")
                        .createTime(Instant.now())
                        .build())
                .user(getUserEntity())
                .build();
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

}
