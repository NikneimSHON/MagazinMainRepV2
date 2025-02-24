package integration;

import entity.PromoCodeEntity;
import entity.UserEntity;
import entity.UserPromoCodeEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;
import util.HibernateTestUtil;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class PromoCodeEntityIT {
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
        var promoCodeEntity = new PromoCodeEntity();

        session.persist(promoCodeEntity);

        assertNotNull(promoCodeEntity.getId());
    }

    @Test
    void get() {
        var promoCodeEntity = getPromoCodeEntity();
        session.persist(promoCodeEntity);

        var resultPromoCodeEntity = session.get(PromoCodeEntity.class, promoCodeEntity.getId());

        assertNotNull(resultPromoCodeEntity);
    }

    @Test
    void delete() {
        var promoCodeEntity = getPromoCodeEntity();

        session.persist(promoCodeEntity);
        session.remove(promoCodeEntity);

        assertNull(session.get(UserPromoCodeEntity.class, promoCodeEntity.getId()));
    }

    @Test
    void update() {
        var promoCodeEntity = getPromoCodeEntity();
        session.persist(promoCodeEntity);

        var promoCode = session.get(PromoCodeEntity.class, promoCodeEntity.getId());
        promoCode.setCode("1000500");
        session.merge(promoCode);

        var resultPromoCodeEntity = session.get(PromoCodeEntity.class, promoCodeEntity.getId());
        assertEquals("1000500", resultPromoCodeEntity.getCode());
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

    private UserPromoCodeEntity getUserPromoCodeEntity() {
        return UserPromoCodeEntity.builder()
                .user(new UserEntity())
                .promoCode(new PromoCodeEntity())
                .usedAt(Instant.now())
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
}
