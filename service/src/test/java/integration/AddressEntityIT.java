package integration;

import entity.*;
import entity.embeddable.AddressInfo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;
import util.HibernateTestUtil;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

public class AddressEntityIT {

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
        var addressEntity = getAddressEntity();


        session.persist(addressEntity);

        assertNotNull(addressEntity.getId());
    }

    @Test
    void update() {
        var addressEntity = getAddressEntity();
        session.persist(addressEntity);

        var address = session.get(AddressEntity.class, addressEntity.getId());
        address.getAddressInfo().setCity("fake");
        session.merge(address);

        var resultAddress = session.get(AddressEntity.class, addressEntity.getId());
        assertEquals("fake", address.getAddressInfo().getCity());
    }

    @Test
    void delete() {
        var addressEntity = getAddressEntity();
        session.persist(addressEntity);

        session.remove(addressEntity);

        assertNull(session.get(AddressEntity.class, addressEntity.getId()));
    }

    @Test
    void get() {
        var addressEntity = getAddressEntity();
        session.persist(addressEntity);

        var address = session.get(AddressEntity.class, addressEntity.getId());

        assertNotNull(address);

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

    private AddressEntity getAddressEntity() {
        return AddressEntity.builder()
                .addressInfo(AddressInfo.builder()
                        .country("Russia")
                        .city("London")
                        .street("fa")
                        .houseNumber(2)
                        .apartmentNumber(23)
                        .build())
                .build();
    }
}
