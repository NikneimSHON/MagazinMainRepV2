package integration.entity;

import com.nikita.shop.entity.PromoCodeEntity;
import com.nikita.shop.entity.UserEntity;
import com.nikita.shop.entity.UserPromoCodeEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.TransactionManager;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserPromoCodeEntityIT extends TransactionManager {

    @Test
    void save() {
        var userPromoCodeEntity = getUserPromoCodeEntity();

        session.persist(userPromoCodeEntity);
        session.flush();
        session.clear();

        assertNotNull(userPromoCodeEntity.getId());
    }

    @Test
    void get() {
        var userPromoCodeEntity = getUserPromoCodeEntity();

        session.persist(userPromoCodeEntity);
        session.flush();
        session.clear();
        var promoCodeEntity = session.get(UserPromoCodeEntity.class, userPromoCodeEntity.getId());

        assertNotNull(promoCodeEntity);
    }

    @Test
    void delete() {
        var userPromoCodeEntity = getUserPromoCodeEntity();

        session.persist(userPromoCodeEntity);
        session.flush();
        session.clear();
        session.remove(userPromoCodeEntity);
        session.flush();
        session.clear();

        Assertions.assertNull(session.get(UserPromoCodeEntity.class, userPromoCodeEntity.getId()));
    }

    @Test
    void update() {
        var userPromoCodeEntity = getUserPromoCodeEntity();
        session.persist(userPromoCodeEntity);
        session.flush();
        session.clear();
        String timestamp = "2023-10-05T12:34:56Z";
        Instant instant = Instant.parse(timestamp);

        var promoCodeEntity = session.get(UserPromoCodeEntity.class, userPromoCodeEntity.getId());
        promoCodeEntity.setUsedAt(instant);
        session.merge(promoCodeEntity);
        session.flush();
        session.clear();

        var resultPromoCodeEntity = session.get(UserPromoCodeEntity.class, userPromoCodeEntity.getId());
        assertNotNull(resultPromoCodeEntity);
        Assertions.assertEquals(timestamp, resultPromoCodeEntity.getUsedAt().toString());

    }

    private UserPromoCodeEntity getUserPromoCodeEntity() {
        return UserPromoCodeEntity.builder()
                .user(new UserEntity())
                .promoCode(new PromoCodeEntity())
                .usedAt(Instant.now())
                .build();
    }
}
