package util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;


public class TransactionManager {

    private static SessionFactory sessionFactory;
    protected Session session;
    protected Transaction transaction;

    @BeforeAll
    static void openSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = HibernateTestUtil.buildSessionFactory();
        }
    }

    @BeforeEach
    void openSessionAndTransaction() {
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
    }

    @AfterEach
    void closeSessionAndRollbackTransaction() {
        if (transaction != null && transaction.isActive()) {
            transaction.rollback();
        }

        if (session != null && session.isOpen()) {
            session.close();
        }

        session = null;
        transaction = null;
    }

    @AfterAll
    static void closeSessionFactory() {
        if (sessionFactory != null) {
            sessionFactory.close();
            sessionFactory = null;
        }
    }
}
