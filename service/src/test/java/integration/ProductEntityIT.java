package integration;

import entity.ProductEntity;
import entity.embeddable.ProductInfo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;
import util.HibernateTestUtil;

import java.math.BigDecimal;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProductEntityIT {
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
        ProductEntity productEntity = getProduct();

        session.persist(productEntity);

        assertNotNull(productEntity.getId());
    }

    @Test
    void get() {
        ProductEntity productEntity = getProduct();
        session.persist(productEntity);

        ProductEntity retrievedProductEntity = session.get(ProductEntity.class, productEntity.getId());

        assertNotNull(retrievedProductEntity);
    }

    @Test
    void update() {
        ProductEntity productEntity = getProduct();
        session.persist(productEntity);

        var product = session.get(ProductEntity.class, productEntity.getId());
        product.setAvailable(false);
        session.merge(product);

        var resultProductEntity = session.get(ProductEntity.class, productEntity.getId());
        assertFalse(resultProductEntity.isAvailable());
    }

    @Test
    void delete() {
        ProductEntity productEntity = getProduct();
        session.persist(productEntity);

        session.remove(productEntity);

        assertNull(session.get(ProductEntity.class, productEntity.getId()));

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

    private ProductEntity getProduct() {
        return ProductEntity.builder()
                .productInfo(ProductInfo.builder()
                        .price(new BigDecimal("100"))
                        .productImage("f")
                        .category("f")
                        .count(4)
                        .description("fa")
                        .build())
                .createTime(Instant.now())
                .isAvailable(true)
                .build();
    }
}
