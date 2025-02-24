package integration;

import entity.AddressEntity;
import entity.PromoCodeEntity;
import entity.UserEntity;
import entity.UserPromoCodeEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;
import util.HibernateTestUtil;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserPromoCodeEntityIT {
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
        var userPromoCodeEntity = getUserPromoCodeEntity();

        session.persist(userPromoCodeEntity);

        assertNotNull(userPromoCodeEntity.getId());
    }

    @Test
    void get() {
        var userPromoCodeEntity = getUserPromoCodeEntity();

        session.persist(userPromoCodeEntity);
        var promoCodeEntity = session.get(UserPromoCodeEntity.class, userPromoCodeEntity.getId());

        assertNotNull(promoCodeEntity);
    }

    @Test
    void delete() {
        var userPromoCodeEntity = getUserPromoCodeEntity();

        session.persist(userPromoCodeEntity);
        session.remove(userPromoCodeEntity);

        assertNull(session.get(UserPromoCodeEntity.class, userPromoCodeEntity.getId()));
    }

    @Test
    void update() {
        var userPromoCodeEntity = getUserPromoCodeEntity();
        session.persist(userPromoCodeEntity);
        String timestamp = "2023-10-05T12:34:56Z";
        Instant instant = Instant.parse(timestamp);

        var promoCodeEntity = session.get(UserPromoCodeEntity.class, userPromoCodeEntity.getId());
        promoCodeEntity.setUsedAt(instant);
        session.merge(promoCodeEntity);

        var resultPromoCodeEntity = session.get(UserPromoCodeEntity.class, userPromoCodeEntity.getId());
        assertNotNull(resultPromoCodeEntity);
        assertEquals(timestamp, resultPromoCodeEntity.getUsedAt().toString());

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
}
