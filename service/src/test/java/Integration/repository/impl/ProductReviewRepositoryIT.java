package Integration.repository.impl;

import Integration.annotation.IT;
import Integration.util.ContainerStarter;
import com.nikita.shop.entity.Activity;
import com.nikita.shop.entity.ProductEntity;
import com.nikita.shop.entity.ProductReviewEntity;
import com.nikita.shop.entity.Role;
import com.nikita.shop.entity.UserEntity;
import com.nikita.shop.entity.embeddable.ProductCategory;
import com.nikita.shop.entity.embeddable.ProductInfo;
import com.nikita.shop.entity.embeddable.ProductReviewInfo;
import com.nikita.shop.entity.embeddable.UserActivity;
import com.nikita.shop.entity.embeddable.UserCredentials;
import com.nikita.shop.entity.embeddable.UserPersonalInfo;
import com.nikita.shop.database.repository.impl.ProductRepository;
import com.nikita.shop.database.repository.impl.ProductReviewRepository;
import com.nikita.shop.database.repository.impl.UserRepository;
import com.nikita.shop.database.filter.ProductReviewFilter;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IT
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
public class ProductReviewRepositoryIT extends ContainerStarter {

    private final EntityManager entityManager;
    private final ProductReviewRepository productReviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @BeforeEach
    void setUp() {
        entityManager.clear();
    }

    @Test
    void save() {
        UserEntity user = userRepository.save(getUserEntity());
        ProductEntity product = productRepository.save(getProductEntity(ProductCategory.BABY_PRODUCTS));
        ProductReviewEntity review = productReviewRepository.save(getProductReviewEntity(user, product));

        entityManager.flush();
        entityManager.clear();

        ProductReviewEntity foundReview = productReviewRepository.findById(review.getId()).orElseThrow();

        assertEquals(review.getId(), foundReview.getId());
        assertEquals(user.getId(), foundReview.getUser().getId());
        assertEquals(product.getId(), foundReview.getProduct().getId());
    }

    @Test
    void findById() {
        UserEntity user = userRepository.save(getUserEntity());
        ProductEntity product = productRepository.save(getProductEntity(ProductCategory.BABY_PRODUCTS));
        ProductReviewEntity review = productReviewRepository.save(getProductReviewEntity(user, product));

        entityManager.flush();
        entityManager.clear();

        Optional<ProductReviewEntity> foundReview = productReviewRepository.findById(review.getId());

        assertTrue(foundReview.isPresent());
        assertEquals(review.getId(), foundReview.get().getId());
    }

    @Test
    void findAll() {
        UserEntity user = userRepository.save(getUserEntity());
        ProductEntity product = productRepository.save(getProductEntity(ProductCategory.BABY_PRODUCTS));

        productReviewRepository.save(getProductReviewEntity(user, product));
        productReviewRepository.save(getProductReviewEntity2(user, product));

        entityManager.flush();
        entityManager.clear();

        List<ProductReviewEntity> reviews = productReviewRepository.findAll();

        assertEquals(2, reviews.size());
    }

    @Test
    void update() {
        UserEntity user = userRepository.save(getUserEntity());
        ProductEntity product = productRepository.save(getProductEntity(ProductCategory.BABY_PRODUCTS));
        ProductReviewEntity review = productReviewRepository.save(getProductReviewEntity(user, product));

        String newDescription = "Updated description";
        review.getProductReviewInfo().setDescription(newDescription);
        productReviewRepository.save(review);

        entityManager.flush();
        entityManager.clear();

        ProductReviewEntity updatedReview = productReviewRepository.findById(review.getId()).orElseThrow();

        assertEquals(newDescription, updatedReview.getProductReviewInfo().getDescription());
    }

    @Test
    void delete() {
        UserEntity user = userRepository.save(getUserEntity());
        ProductEntity product = productRepository.save(getProductEntity(ProductCategory.BABY_PRODUCTS));
        ProductReviewEntity review = productReviewRepository.save(getProductReviewEntity(user, product));

        productReviewRepository.delete(review);

        entityManager.flush();
        entityManager.clear();

        Optional<ProductReviewEntity> deletedReview = productReviewRepository.findById(review.getId());

        assertTrue(deletedReview.isEmpty());
    }

    @Test
    void deleteProductReviewByUserId() {
        UserEntity user = userRepository.save(getUserEntity());
        ProductEntity product = productRepository.save(getProductEntity(ProductCategory.BABY_PRODUCTS));
        ProductReviewEntity review = productReviewRepository.save(getProductReviewEntity(user, product));

        entityManager.flush();
        entityManager.clear();

        List<ProductReviewEntity> deletedReviews = productReviewRepository.deleteProductReviewByUserId(user.getId());
        Optional<ProductReviewEntity> foundReview = productReviewRepository.findById(review.getId());

        assertEquals(1, deletedReviews.size());
        assertTrue(foundReview.isEmpty());
    }

    @Test
    void deleteProductReviewByProductId() {
        UserEntity user = userRepository.save(getUserEntity());
        ProductEntity product = productRepository.save(getProductEntity(ProductCategory.BABY_PRODUCTS));
        ProductReviewEntity review = productReviewRepository.save(getProductReviewEntity(user, product));

        entityManager.flush();
        entityManager.clear();

        List<ProductReviewEntity> deletedReviews = productReviewRepository.deleteProductReviewByProductId(product.getId());
        Optional<ProductReviewEntity> foundReview = productReviewRepository.findById(review.getId());

        assertEquals(1, deletedReviews.size());
        assertTrue(foundReview.isEmpty());
    }

    @Test
    void findAllByFilter_NoFilter() {
        UserEntity user = userRepository.save(getUserEntity());
        ProductEntity product = productRepository.save(getProductEntity(ProductCategory.BABY_PRODUCTS));

        productReviewRepository.save(getProductReviewEntity(user, product));
        productReviewRepository.save(getProductReviewEntity2(user, product));

        ProductReviewFilter filter = ProductReviewFilter.builder().build();
        Pageable pageable = PageRequest.of(0, 10);

        entityManager.flush();
        entityManager.clear();

        Page<ProductReviewEntity> resultPage = productReviewRepository.findAllByFilter(filter, pageable);

        assertEquals(2, resultPage.getTotalElements());
        assertTrue(resultPage.hasContent());
    }

    @Test
    void findAllByFilter_WithFilter() {
        UserEntity user = userRepository.save(getUserEntity());
        ProductEntity product = productRepository.save(getProductEntity(ProductCategory.BABY_PRODUCTS));

        ProductReviewEntity reviewWithRating5 = productReviewRepository.save(getProductReviewEntity(user, product));
        productReviewRepository.save(getProductReviewEntity2(user, product));


        ProductReviewFilter filter = ProductReviewFilter.builder()
                .rating(5)
                .build();
        Pageable pageable = PageRequest.of(0, 10);

        entityManager.flush();
        entityManager.clear();

        Page<ProductReviewEntity> resultPage = productReviewRepository.findAllByFilter(filter, pageable);


        assertEquals(1, resultPage.getTotalElements());
        assertEquals(reviewWithRating5.getId(), resultPage.getContent().get(0).getId());
        assertEquals(5, resultPage.getContent().get(0).getProductReviewInfo().getRating());
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
                .userActivity(UserActivity.builder()
                        .registrationDate(Instant.now())
                        .activity(Activity.ALLOWED)
                        .lastLogin(Instant.now())
                        .build())
                .build();
    }

    private ProductReviewEntity getProductReviewEntity(UserEntity user, ProductEntity product) {
        return ProductReviewEntity.builder()
                .product(product)
                .user(user)
                .productReviewInfo(ProductReviewInfo.builder()
                        .createTime(Instant.now())
                        .rating(5)
                        .description("fake")
                        .image("fake")
                        .build())
                .build();
    }

    private ProductReviewEntity getProductReviewEntity2(UserEntity user, ProductEntity product) {
        return ProductReviewEntity.builder()
                .product(product)
                .user(user)
                .productReviewInfo(ProductReviewInfo.builder()
                        .createTime(Instant.now())
                        .rating(6)
                        .description("ffake")
                        .image("ffake")
                        .build())
                .build();
    }

    private ProductEntity getProductEntity(ProductCategory category) {
        return ProductEntity.builder()
                .productInfo(ProductInfo.builder()
                        .productImage("F")
                        .count(2)
                        .name("UNIQUE")
                        .description("Fake")
                        .price(212)
                        .category(category)
                        .build())
                .available(true)
                .createAt(Instant.now())
                .build();
    }
}
