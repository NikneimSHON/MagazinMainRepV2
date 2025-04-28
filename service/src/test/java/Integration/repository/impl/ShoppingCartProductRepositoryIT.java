package Integration.repository.impl;

import Integration.annotation.IT;
import Integration.util.ContainerStarter;
import com.nikita.shop.entity.Activity;
import com.nikita.shop.entity.OrderStatus;
import com.nikita.shop.entity.ProductEntity;
import com.nikita.shop.entity.Role;
import com.nikita.shop.entity.ShoppingCartEntity;
import com.nikita.shop.entity.ShoppingCartProductEntity;
import com.nikita.shop.entity.UserEntity;
import com.nikita.shop.entity.embeddable.ProductCategory;
import com.nikita.shop.entity.embeddable.ProductInfo;
import com.nikita.shop.entity.embeddable.ShoppingCartDate;
import com.nikita.shop.entity.embeddable.UserActivity;
import com.nikita.shop.entity.embeddable.UserCredentials;
import com.nikita.shop.entity.embeddable.UserPersonalInfo;
import com.nikita.shop.database.repository.impl.ProductRepository;
import com.nikita.shop.database.repository.impl.ShoppingCartProductRepository;
import com.nikita.shop.database.repository.impl.ShoppingCartRepository;
import com.nikita.shop.database.repository.impl.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IT
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
public class ShoppingCartProductRepositoryIT extends ContainerStarter {
    private final EntityManager entityManager;
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ShoppingCartProductRepository shoppingCartProductRepository;

    @BeforeEach
    void setUp() {
        entityManager.clear();
    }

    @Test
    void save(){
        ShoppingCartProductEntity shoppingCartProductEntity = getShoppingCartProductEntity(10);
        shoppingCartProductRepository.save(shoppingCartProductEntity);
        entityManager.flush();
        entityManager.clear();

        Optional<ShoppingCartProductEntity> result = shoppingCartProductRepository.findById(shoppingCartProductEntity.getId());

        assertTrue(result.isPresent());
    }

    @Test
    void update(){
        ShoppingCartProductEntity shoppingCartProductEntity = getShoppingCartProductEntity(10);
        shoppingCartProductRepository.save(shoppingCartProductEntity);
        entityManager.flush();
        entityManager.clear();

        Optional<ShoppingCartProductEntity> shoppingCartProduct = shoppingCartProductRepository.findById(shoppingCartProductEntity.getId());
        ShoppingCartProductEntity updatedShoppingCartProductEntity = shoppingCartProduct.get();
        updatedShoppingCartProductEntity.setCountProduct(11);
        shoppingCartProductRepository.save(updatedShoppingCartProductEntity);

        entityManager.flush();
        entityManager.clear();

        Optional<ShoppingCartProductEntity> result = shoppingCartProductRepository.findById(shoppingCartProductEntity.getId());
        assertTrue(result.isPresent());
        assertEquals(result.get().getCountProduct(), 11);
    }

    @Test
    void delete(){
        ShoppingCartProductEntity shoppingCartProductEntity = getShoppingCartProductEntity(10);
        shoppingCartProductRepository.save(shoppingCartProductEntity);
        entityManager.flush();
        entityManager.clear();

        Optional<ShoppingCartProductEntity> result = shoppingCartProductRepository.findById(shoppingCartProductEntity.getId());

        assertFalse(result.isPresent());
    }

    @Test
    void findAll(){
        ShoppingCartProductEntity shoppingCartProductEntity1 = getShoppingCartProductEntity(10);
        ShoppingCartProductEntity shoppingCartProductEntity2 = getShoppingCartProductEntity(10);

        shoppingCartProductRepository.save(shoppingCartProductEntity1);
        shoppingCartProductRepository.save(shoppingCartProductEntity2);
        entityManager.flush();
        entityManager.clear();

        List<ShoppingCartProductEntity> result = shoppingCartProductRepository.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void findById(){
        ShoppingCartProductEntity shoppingCartProductEntity = getShoppingCartProductEntity(10);
        shoppingCartProductRepository.save(shoppingCartProductEntity);
        entityManager.flush();
        entityManager.clear();

        Optional<ShoppingCartProductEntity> result = shoppingCartProductRepository.findById(shoppingCartProductEntity.getId());

        assertTrue(result.isPresent());
        assertEquals(result.get().getId(), shoppingCartProductEntity.getId());
    }


    private ShoppingCartProductEntity getShoppingCartProductEntity(Integer count) {
        ProductEntity productEntity = getProductEntity(ProductCategory.BABY_PRODUCTS);
        productRepository.save(productEntity);

        UserEntity userEntity = getUserEntity();
        userRepository.save(userEntity);

        ShoppingCartEntity shoppingCartEntity = getShoppingCartEntity(userEntity);
        shoppingCartRepository.save(shoppingCartEntity);

        return ShoppingCartProductEntity.builder()
                .cart(shoppingCartEntity)
                .product(productEntity)
                .countProduct(count)
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


    private UserEntity getUserEntity() {
        return UserEntity.builder()
                .role(Role.ADMIN)
                .phoneNumber("895200011")
                .personalInfo(UserPersonalInfo.builder()
                        .image("fake")
                        .birthDate(LocalDate.of(2000, 10, 12))
                        .lastname("fake")
                        .firstname("fake")
                        .build())
                .credentials(UserCredentials.builder()
                        .password("fake")
                        .emailVerified(true)
                        .email("fake2@mail.ru")
                        .build())
                .userActivity(UserActivity.builder()
                        .registrationDate(Instant.now())
                        .activity(Activity.ALLOWED)
                        .lastLogin(Instant.now())
                        .build())
                .build();
    }

    private ShoppingCartEntity getShoppingCartEntity(UserEntity user) {
        return ShoppingCartEntity.builder()
                .shoppingCartDate(ShoppingCartDate.builder()
                        .creatTime(Instant.now())
                        .updateTime(Instant.now())
                        .build())
                .user(user)
                .orderStatus(OrderStatus.BASKET)
                .build();
    }


}
