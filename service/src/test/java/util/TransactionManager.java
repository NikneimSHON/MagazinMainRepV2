package util;

import config.ApplicationTestConfiguration;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public abstract class TransactionManager {

    protected Session session = context.getBean(Session.class);
    protected Transaction transaction;
    protected static final AnnotationConfigApplicationContext context;

    static {
        context = new AnnotationConfigApplicationContext(ApplicationTestConfiguration.class);
    }

    @BeforeEach
    void openSessionAndTransaction() {
        transaction = session.beginTransaction();
    }
        // проблема, я хлтел убрать методы openSessionAndTransaction и closeSessionAndRollbackTransaction
    // и указывать просто @Transaction в RepositoryBase но для критерии, которая используется нужна активная транзакция,
    // и получается тест падает, что в таком случае сделать можно? переписать прокси?
    @AfterEach
    void closeSessionAndRollbackTransaction() {
        if (transaction != null && transaction.isActive()) {
            transaction.rollback();
        }

    }

}
