package integration;

import entity.ProductEntity;
import entity.ShoppingCartEntity;
import entity.ShoppingCartProductEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;
import util.HibernateTestUtil;

import static org.junit.jupiter.api.Assertions.*;

public class ShoppingCartProductIEntityIT {
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
        var shoppingCartProductEntity = getShoppingCartProduct();

        session.persist(shoppingCartProductEntity);

        assertNotNull(shoppingCartProductEntity.getId());
    }

    @Test
    void update() {
        var shoppingCartProductEntity = getShoppingCartProduct();
        session.persist(shoppingCartProductEntity);

        var shoppingCartProduct = session.get(ShoppingCartProductEntity.class, shoppingCartProductEntity.getId());
        shoppingCartProduct.setCountProduct(19999);
        session.merge(shoppingCartProduct);

        assertEquals(19999, shoppingCartProductEntity.getCountProduct());
    }

    @Test
    void delete() {
        var shoppingCartProductEntity = getShoppingCartProduct();
        session.persist(shoppingCartProductEntity);

        session.remove(shoppingCartProductEntity);

        assertNull(session.get(ShoppingCartProductEntity.class, shoppingCartProductEntity.getId()));
    }

    @Test
    void get() {
        var shoppingCartProductEntity = getShoppingCartProduct();

        session.persist(shoppingCartProductEntity);

        assertNotNull(shoppingCartProductEntity.getId());
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

    private ShoppingCartProductEntity getShoppingCartProduct() {
        return ShoppingCartProductEntity.builder()
                .cart(new ShoppingCartEntity())
                .countProduct(10)
                .product(new ProductEntity())
                .build();
    }
}
