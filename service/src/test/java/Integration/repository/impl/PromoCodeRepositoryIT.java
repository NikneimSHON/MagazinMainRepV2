package Integration.repository.impl;

import Integration.annotation.IT;
import Integration.util.ContainerStarter;
import com.nikita.shop.entity.PromoCodeEntity;
import com.nikita.shop.database.repository.impl.PromoCodeRepository;
import com.nikita.shop.database.filter.PromoCodeFilter;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestConstructor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IT
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
public class PromoCodeRepositoryIT extends ContainerStarter {

    private final EntityManager entityManager;
    private final PromoCodeRepository promoCodeRepository;

    @BeforeEach
    void setUp() {
        entityManager.clear();
    }

    @Test
    void save() {
        PromoCodeEntity promoCodeEntity = getPromoCode("Code");
        PromoCodeEntity promoCode = promoCodeRepository.save(promoCodeEntity);
        entityManager.flush();
        entityManager.clear();

        Optional<PromoCodeEntity> optionalPromoCode = promoCodeRepository.findById(promoCode.getId());

        assertTrue(optionalPromoCode.isPresent());
        assertEquals(optionalPromoCode.get().getId(), promoCode.getId());
    }

    @Test
    void update() {
        PromoCodeEntity promoCode = promoCodeRepository.save(getPromoCode("Code"));
        entityManager.flush();
        entityManager.clear();

        String newCode = "new_code";
        promoCode.setCode(newCode);
        promoCode.setDiscountSum(200);
        PromoCodeEntity updatedPromoCode = promoCodeRepository.save(promoCode);
        entityManager.flush();
        entityManager.clear();

        Optional<PromoCodeEntity> foundPromoCode = promoCodeRepository.findById(updatedPromoCode.getId());
        assertTrue(foundPromoCode.isPresent());
        assertEquals(newCode, foundPromoCode.get().getCode());
        assertEquals(200, foundPromoCode.get().getDiscountSum());
    }

    @Test
    void findAll() {
        promoCodeRepository.deleteAll();

        promoCodeRepository.save(getPromoCode("Code1"));
        promoCodeRepository.save(getPromoCode("Code2"));
        entityManager.flush();
        entityManager.clear();

        List<PromoCodeEntity> allPromoCodes = promoCodeRepository.findAll();

        assertEquals(2, allPromoCodes.size());//достаточно что только размер проверяю?
    }

    @Test
    void findById() {
        PromoCodeEntity promoCode = promoCodeRepository.save(getPromoCode("code"));
        entityManager.flush();
        entityManager.clear();

        Optional<PromoCodeEntity> foundPromoCode = promoCodeRepository.findById(promoCode.getId());

        assertTrue(foundPromoCode.isPresent());
        assertEquals(promoCode.getId(), foundPromoCode.get().getId());
        assertEquals("code", foundPromoCode.get().getCode());
    }

    @Test
    void delete() {
        PromoCodeEntity promoCode = promoCodeRepository.save(getPromoCode("code"));
        entityManager.flush();
        entityManager.clear();

        promoCodeRepository.deleteById(promoCode.getId());
        entityManager.flush();
        entityManager.clear();

        assertFalse(promoCodeRepository.findById(promoCode.getId()).isPresent());
    }

    @Test
    void findAllByFilter_NoFilter(){
        promoCodeRepository.save(promoCodeRepository.save(getPromoCode("code1")));
        promoCodeRepository.save(promoCodeRepository.save(getPromoCode("code2")));
        PromoCodeFilter filter = PromoCodeFilter.builder().build();
        Pageable pageable = PageRequest.of(0, 10);

        entityManager.flush();
        entityManager.clear();

        Page<PromoCodeEntity> result = promoCodeRepository.findAllByFilter(filter,pageable);

        assertEquals(result.getTotalElements(), 2);

    }

    @Test
    void findAllByFilter_WithFilter(){
        promoCodeRepository.save(promoCodeRepository.save(getPromoCode2(true)));
        promoCodeRepository.save(promoCodeRepository.save(getPromoCode2(false)));
        PromoCodeFilter filter = PromoCodeFilter.builder()
                .activityPromo(false)
                .build();
        Pageable pageable = PageRequest.of(0, 10);

        entityManager.flush();
        entityManager.clear();

        Page<PromoCodeEntity> result = promoCodeRepository.findAllByFilter(filter,pageable);

        assertEquals(result.getTotalElements(), 1);

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
    private PromoCodeEntity getPromoCode2(Boolean activityPromo) {
        return PromoCodeEntity.builder()
                .code("code")
                .activityPromo(activityPromo)
                .discountSum(100)
                .validTo(Instant.now().plus(1, ChronoUnit.DAYS))
                .minOrderAmount(1)
                .validFrom(Instant.now())
                .remainingQuantity(10)
                .build();
    }

}
