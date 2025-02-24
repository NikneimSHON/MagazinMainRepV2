package integration;

import entity.ShoppingCartEntity;
import entity.embeddable.ShoppingCartDate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;
import util.HibernateTestUtil;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ShoppingCartEntityIT {
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
        ShoppingCartEntity shoppingCartEntity = getShoppingCart();

        session.persist(shoppingCartEntity);

        assertNotNull(shoppingCartEntity.getId());
    }

    @Test
    void get() {
        ShoppingCartEntity shoppingCartEntity = getShoppingCart();

        session.persist(shoppingCartEntity);

        assertNotNull(shoppingCartEntity.getId());
    }

    @Test
    void update() {
        ShoppingCartEntity shoppingCartEntity = getShoppingCart();
        session.persist(shoppingCartEntity);

        var shoppingCart = session.get(ShoppingCartEntity.class, shoppingCartEntity.getId());
        shoppingCart.setOrderStatus("topFAKE");
        session.merge(shoppingCart);

        assertEquals("topFAKE", shoppingCart.getOrderStatus());
    }

    @Test
    void delete() {
        ShoppingCartEntity shoppingCartEntity = getShoppingCart();
        session.persist(shoppingCartEntity);

        session.remove(shoppingCartEntity);

        assertNull(session.get(ShoppingCartEntity.class, shoppingCartEntity.getId()));
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

    private ShoppingCartEntity getShoppingCart() {
        return ShoppingCartEntity.builder()
                .orderStatus("f")
                .shoppingCartDate(ShoppingCartDate.builder()
                        .updateTime(Instant.now())
                        .creationTime(Instant.now())
                        .build())
                .build();
    }
}
