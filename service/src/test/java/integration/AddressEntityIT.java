package integration;

import com.nikita.shop.entity.AddressEntity;
import com.nikita.shop.entity.embeddable.AddressInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.TransactionManager;

import static org.junit.jupiter.api.Assertions.assertNull;

public class AddressEntityIT extends TransactionManager {

    @Test
    void save() {
        var addressEntity = getAddressEntity();

        session.persist(addressEntity);
        session.flush();
        session.clear();

        var savedAddress = session.get(AddressEntity.class, addressEntity.getId());
        Assertions.assertNotNull(savedAddress);
    }

    @Test
    void update() {
        var addressEntity = getAddressEntity();
        session.persist(addressEntity);
        session.flush();
        session.clear();

        var address = session.get(AddressEntity.class, addressEntity.getId());
        address.getAddressInfo().setCity("fake");
        session.merge(address);
        session.flush();
        session.clear();

        var resultAddress = session.get(AddressEntity.class, addressEntity.getId());
        Assertions.assertEquals("fake", resultAddress.getAddressInfo().getCity());
    }

    @Test
    void delete() {
        var addressEntity = getAddressEntity();
        session.persist(addressEntity);
        session.flush();
        session.clear();

        var addressToDelete = session.get(AddressEntity.class, addressEntity.getId());
        session.remove(addressToDelete);
        session.flush();
        session.clear();

        assertNull(session.get(AddressEntity.class, addressEntity.getId()));
    }

    @Test
    void get() {
        var addressEntity = getAddressEntity();
        session.persist(addressEntity);
        session.flush();
        session.clear();

        var address = session.get(AddressEntity.class, addressEntity.getId());
        Assertions.assertNotNull(address);
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
