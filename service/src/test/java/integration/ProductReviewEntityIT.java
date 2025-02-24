package integration;

import entity.ProductReviewEntity;
import entity.embeddable.ProductReviewInfo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;
import util.HibernateTestUtil;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class ProductReviewEntityIT {
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
        var productReviewEntity = getProductReviewEntity();

        session.persist(productReviewEntity);

        assertNotNull(productReviewEntity.getId());
    }

    @Test
    void get() {
        var productReviewEntity = getProductReviewEntity();

        session.persist(productReviewEntity);
        var productReview = session.get(ProductReviewEntity.class, productReviewEntity.getId());

        assertNotNull(productReview);
    }

    @Test
    void delete() {
        var productReviewEntity = getProductReviewEntity();

        session.persist(productReviewEntity);
        session.remove(productReviewEntity);

        assertNull(session.get(ProductReviewEntity.class, productReviewEntity.getId()));
    }

    @Test
    void update() {
        var productReviewEntity = getProductReviewEntity();
        session.persist(productReviewEntity);

        var productReview = session.get(ProductReviewEntity.class, productReviewEntity.getId());
        productReview.getProductReviewInfo().setImage("fake");
        session.merge(productReviewEntity);

        assertEquals("fake", productReview.getProductReviewInfo().getImage());
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

    private ProductReviewEntity getProductReviewEntity() {
        return ProductReviewEntity.builder()
                .productReviewInfo(ProductReviewInfo.builder()
                        .rating(5)
                        .image("f")
                        .description("f")
                        .createTime(Instant.now())
                        .build())
                .build();
    }
}
