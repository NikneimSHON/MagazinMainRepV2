package integration;

import com.nikita.shop.entity.PromoCodeEntity;

import com.nikita.shop.entity.UserPromoCodeEntity;
import org.junit.jupiter.api.Test;
import util.TransactionManager;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class PromoCodeEntityIT extends TransactionManager {
    @Test
    void save() {
        var promoCodeEntity = new PromoCodeEntity();

        session.persist(promoCodeEntity);
        session.flush();
        session.clear();

        assertNotNull(promoCodeEntity.getId());
    }

    @Test
    void get() {
        var promoCodeEntity = getPromoCodeEntity();
        session.persist(promoCodeEntity);
        session.flush();
        session.clear();

        var resultPromoCodeEntity = session.get(PromoCodeEntity.class, promoCodeEntity.getId());

        assertNotNull(resultPromoCodeEntity);
    }

    @Test
    void delete() {
        var promoCodeEntity = getPromoCodeEntity();

        session.persist(promoCodeEntity);
        session.flush();
        session.clear();
        session.remove(promoCodeEntity);
        session.flush();
        session.clear();

        assertNull(session.get(UserPromoCodeEntity.class, promoCodeEntity.getId()));
    }

    @Test
    void update() {
        var promoCodeEntity = getPromoCodeEntity();
        session.persist(promoCodeEntity);
        session.flush();
        session.clear();

        var promoCode = session.get(PromoCodeEntity.class, promoCodeEntity.getId());
        promoCode.setCode("1000500");
        session.merge(promoCode);
        session.flush();
        session.clear();

        var resultPromoCodeEntity = session.get(PromoCodeEntity.class, promoCodeEntity.getId());
        assertEquals("1000500", resultPromoCodeEntity.getCode());
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
}
