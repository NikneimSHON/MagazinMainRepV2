package Integration.repository.impl;

import Integration.annotation.IT;
import Integration.util.ContainerStarter;
import com.nikita.shop.entity.Activity;
import com.nikita.shop.entity.OrderStatus;
import com.nikita.shop.entity.Role;
import com.nikita.shop.entity.ShoppingCartEntity;
import com.nikita.shop.entity.UserEntity;
import com.nikita.shop.entity.embeddable.ShoppingCartDate;
import com.nikita.shop.entity.embeddable.UserActivity;
import com.nikita.shop.entity.embeddable.UserCredentials;
import com.nikita.shop.entity.embeddable.UserPersonalInfo;
import com.nikita.shop.database.repository.impl.ShoppingCartRepository;
import com.nikita.shop.database.repository.impl.UserRepository;
import com.nikita.shop.database.filter.ShoppingCartFilter;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IT
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
public class ShoppingCartRepositoryIT extends ContainerStarter {
    private final EntityManager entityManager;
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;


    @BeforeEach
    void setUp() {
        entityManager.clear();
    }

    @Test
    void save() {
        UserEntity user = userRepository.save(getUserEntity());
        ShoppingCartEntity ShoppingCart = shoppingCartRepository.save(getShoppingCartEntity(user));
        entityManager.flush();
        entityManager.clear();

        Optional<ShoppingCartEntity> result = shoppingCartRepository.findById(ShoppingCart.getId());

        assertTrue(result.isPresent());
    }

    @Test
    void update() {
        UserEntity user = userRepository.save(getUserEntity());
        ShoppingCartEntity cart = shoppingCartRepository.save(getShoppingCartEntity(user));
        entityManager.flush();
        entityManager.clear();

        Optional<ShoppingCartEntity> shoppingCart = shoppingCartRepository.findById(cart.getId());
        ShoppingCartEntity updatedCart = shoppingCart.get();
        updatedCart.setOrderStatus(OrderStatus.CANCELLED);
        shoppingCartRepository.save(updatedCart);

        entityManager.flush();
        entityManager.clear();

        Optional<ShoppingCartEntity> result = shoppingCartRepository.findById(cart.getId());

        assertTrue(result.isPresent());
        assertEquals(OrderStatus.CANCELLED, result.get().getOrderStatus());
    }

    @Test
    void delete() {
        UserEntity user = userRepository.save(getUserEntity());
        ShoppingCartEntity cart = shoppingCartRepository.save(getShoppingCartEntity(user));
        entityManager.flush();
        entityManager.clear();

        shoppingCartRepository.deleteById(cart.getId());

        List<ShoppingCartEntity> result = shoppingCartRepository.findAll();

        assertEquals(0, result.size());
    }

    @Test
    void findAll() {
        UserEntity user = userRepository.save(getUserEntity());
        shoppingCartRepository.save(getShoppingCartEntity(user));
        entityManager.flush();
        entityManager.clear();

        List<ShoppingCartEntity> result = shoppingCartRepository.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void findById() {
        UserEntity user = userRepository.save(getUserEntity());
        ShoppingCartEntity cart = shoppingCartRepository.save(getShoppingCartEntity(user));
        entityManager.flush();
        entityManager.clear();

        Optional<ShoppingCartEntity> result = shoppingCartRepository.findById(cart.getId());

        assertTrue(result.isPresent());
        assertEquals(cart.getId(), result.get().getId());
    }

    @Test
    void findAllByFilter_NoFilter() {
        UserEntity user = userRepository.save(getUserEntity());
        ShoppingCartEntity shoppingCart = shoppingCartRepository.save(getShoppingCartEntity(user));

        ShoppingCartFilter filter = ShoppingCartFilter.builder().build();
        Pageable pageable = PageRequest.of(0, 10);

        entityManager.flush();
        entityManager.clear();

        Page<ShoppingCartEntity> result = shoppingCartRepository.findAllByFilter(filter,pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void findAllByFilter_WithFilter() {
        UserEntity user = userRepository.save(getUserEntity());
        ShoppingCartEntity shoppingCart = shoppingCartRepository.save(getShoppingCartEntity(user,OrderStatus.BASKET));

        ShoppingCartFilter filter = ShoppingCartFilter.builder()
                .status(OrderStatus.ASSEMBLY)
                .build();
        Pageable pageable = PageRequest.of(0, 10);

        entityManager.flush();
        entityManager.clear();

        Page<ShoppingCartEntity> result = shoppingCartRepository.findAllByFilter(filter,pageable);

        assertEquals(0, result.getTotalElements());
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

    private ShoppingCartEntity getShoppingCartEntity(UserEntity user,OrderStatus orderStatus) {
        return ShoppingCartEntity.builder()
                .shoppingCartDate(ShoppingCartDate.builder()
                        .creatTime(Instant.now())
                        .updateTime(Instant.now())
                        .build())
                .user(user)
                .orderStatus(orderStatus)
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

}
