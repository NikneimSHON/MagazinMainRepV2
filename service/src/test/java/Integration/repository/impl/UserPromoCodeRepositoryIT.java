package Integration.repository.impl;

import Integration.annotation.IT;
import Integration.util.ContainerStarter;
import com.nikita.shop.entity.Activity;
import com.nikita.shop.entity.PromoCodeEntity;
import com.nikita.shop.entity.Role;
import com.nikita.shop.entity.UserEntity;
import com.nikita.shop.entity.UserPromoCodeEntity;
import com.nikita.shop.entity.embeddable.UserActivity;
import com.nikita.shop.entity.embeddable.UserCredentials;
import com.nikita.shop.entity.embeddable.UserPersonalInfo;
import com.nikita.shop.database.repository.impl.PromoCodeRepository;
import com.nikita.shop.database.repository.impl.UserPromoCodeRepository;
import com.nikita.shop.database.repository.impl.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IT
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
public class UserPromoCodeRepositoryIT extends ContainerStarter {

    private final UserPromoCodeRepository userPromoCodeRepository;
    private final UserRepository userRepository;
    private final PromoCodeRepository promoCodeRepository;

    private final EntityManager entityManager;


    @BeforeEach
    void setUp() {
        entityManager.clear();
    }

    @Test
    void save() {
        UserEntity user = userRepository.save(getUserEntity());
        PromoCodeEntity promoCode = promoCodeRepository.save(getPromoCode("PROMO1"));
        UserPromoCodeEntity userPromoCode = getUserPromoCode(user, promoCode);
        userPromoCodeRepository.save(userPromoCode);

        entityManager.flush();
        entityManager.clear();

        Optional<UserPromoCodeEntity> result = userPromoCodeRepository.findById(userPromoCode.getId());

        assertTrue(result.isPresent());
    }

    @Test
    void findById() {
        UserEntity user = userRepository.save(getUserEntity());
        PromoCodeEntity promoCode = promoCodeRepository.save(getPromoCode("PROMO1"));
        UserPromoCodeEntity userPromoCode = getUserPromoCode(user, promoCode);
        userPromoCodeRepository.save(userPromoCode);

        entityManager.flush();
        entityManager.clear();

        Optional<UserPromoCodeEntity> result = userPromoCodeRepository.findById(userPromoCode.getId());

        assertTrue(result.isPresent());
        assertEquals(result.get().getPromoCode().getCode(),"PROMO1");
    }

    @Test
    void findAll() {
        UserEntity user = userRepository.save(getUserEntity());
        PromoCodeEntity promoCode1 = promoCodeRepository.save(getPromoCode("PROMO1"));
        PromoCodeEntity promoCode2 = promoCodeRepository.save(getPromoCode("PROMO2"));
        userPromoCodeRepository.save(getUserPromoCode(user, promoCode1));
        userPromoCodeRepository.save(getUserPromoCode(user, promoCode2));

        entityManager.flush();
        entityManager.clear();

        List<UserPromoCodeEntity> result = userPromoCodeRepository.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void delete() {
        UserEntity user = userRepository.save(getUserEntity());
        PromoCodeEntity promoCode = promoCodeRepository.save(getPromoCode("PROMO1"));
        UserPromoCodeEntity userPromoCode = getUserPromoCode(user, promoCode);
        userPromoCodeRepository.save(userPromoCode);

        userPromoCodeRepository.delete(userPromoCode);

        entityManager.flush();
        entityManager.clear();

        List<UserPromoCodeEntity> result = userPromoCodeRepository.findAll();

        assertEquals(0, result.size());
    }

    @Test
    void update() {
        UserEntity user = userRepository.save(getUserEntity());
        PromoCodeEntity promoCode = promoCodeRepository.save(getPromoCode("PROMO1"));
        UserPromoCodeEntity userPromoCode = getUserPromoCode(user, promoCode);
        userPromoCodeRepository.save(userPromoCode);
        entityManager.flush();
        entityManager.clear();

        Optional<UserPromoCodeEntity> result = userPromoCodeRepository.findById(userPromoCode.getId());
        UserPromoCodeEntity entity = result.get();
        entity.setUsedAt(Instant.parse("2023-01-01T12:00:00Z"));
        userPromoCodeRepository.save(entity);

        entityManager.flush();
        entityManager.clear();

        Optional<UserPromoCodeEntity> resultEntity = userPromoCodeRepository.findById(entity.getId());

        assertTrue(resultEntity.isPresent());
        assertEquals(resultEntity.get().getUsedAt(),Instant.parse("2023-01-01T12:00:00Z"));


    }

    private PromoCodeEntity getPromoCode(String code) {
        return PromoCodeEntity.builder()
                .code(code)
                .activityPromo(true)
                .discountSum(100)
                .validTo(Instant.now().plus(1, ChronoUnit.DAYS))
                .minOrderAmount(1)
                .validFrom(Instant.now())
                .remainingQuantity(10)
                .build();
    }


    private UserPromoCodeEntity getUserPromoCode(UserEntity user, PromoCodeEntity promoCode) {
        return UserPromoCodeEntity.builder()
                .user(user)
                .usedAt(Instant.now())
                .promoCode(promoCode)
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
                .userActivity(UserActivity.builder()
                        .registrationDate(Instant.now())
                        .activity(Activity.ALLOWED)
                        .lastLogin(Instant.now())
                        .build())
                .build();
    }
}
