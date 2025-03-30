package Integration;

import com.nikita.shop.database.repository.impl.AddressRepository;
import com.nikita.shop.database.entity.Activity;
import com.nikita.shop.database.entity.AddressEntity;
import com.nikita.shop.database.entity.Role;
import com.nikita.shop.database.entity.UserEntity;
import com.nikita.shop.database.entity.embeddable.AddressInfo;
import com.nikita.shop.database.entity.embeddable.UserActivity;
import com.nikita.shop.database.entity.embeddable.UserCredentials;
import com.nikita.shop.database.entity.embeddable.UserPersonalInfo;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.TransactionManager;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class RepositoryBaseIT extends TransactionManager {

    private final AddressRepository repository = context.getBean(AddressRepository.class);

    @Test
    void save() {
        var addressResult = repository.save(getAddressEntity());
        Assertions.assertNotNull(addressResult);
    }

    @Test
    void update() {
        AddressEntity address = getAddressEntity();
        repository.save(address);
        address.getAddressInfo().setCity("Fake");
        repository.update(address);

        var addressResult = repository.findById(address.getId());

        Assertions.assertNotNull(addressResult);
        Assertions.assertEquals("Fake", address.getAddressInfo().getCity());

    }

    @Test
    void delete() {
        AddressEntity address = getAddressEntity();
        repository.save(address);

        repository.delete(address);

        Optional<AddressEntity> resultEntity = repository.findById(address.getId());
        Assertions.assertFalse(resultEntity.isPresent());

    }

    @Test
    void findById() {
        AddressEntity address = getAddressEntity();
        repository.save(address);
        session.clear();

        Optional<AddressEntity> resultEntity = repository.findById(address.getId());
        Assertions.assertTrue(resultEntity.isPresent());
    }

    @Test
    void findAll() {
        AddressEntity address = getAddressEntity();
        repository.save(address);
        session.clear();

        List<AddressEntity> resultEntity = repository.findAll();
        Assertions.assertFalse(resultEntity.isEmpty());
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
                .user(getUserEntity())
                .build();
    }

    private UserEntity getUserEntity() {
        return UserEntity.builder()
                .role(Role.ADMIN)
                .phoneNumber("895200000")
                .personalInfo(UserPersonalInfo.builder()
                        .image("f")
                        .birthDate(LocalDate.of(2000, 10, 12))
                        .lastname("F")
                        .firstname("M")
                        .build())
                .credentials(UserCredentials.builder()
                        .password("12456")
                        .emailVerified(true)
                        .email("fafsa@mail.ru")
                        .build())
                .activity(UserActivity.builder()
                        .registrationDate(Instant.now())
                        .activity(Activity.ALLOWED)
                        .lastLogin(Instant.now())
                        .build())
                .build();
    }
}
