package integration.repository;

import com.nikita.shop.repository.impl.AddressRepository;
import com.nikita.shop.entity.Activity;
import com.nikita.shop.entity.AddressEntity;
import com.nikita.shop.entity.Role;
import com.nikita.shop.entity.UserEntity;
import com.nikita.shop.entity.embeddable.AddressInfo;
import com.nikita.shop.entity.embeddable.UserActivity;
import com.nikita.shop.entity.embeddable.UserCredentials;
import com.nikita.shop.entity.embeddable.UserPersonalInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.TransactionManager;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class RepositoryBaseIT extends TransactionManager {
    private AddressRepository repository;

    //Сначало сделал синглтоном AddressRepository но была проблема что когда я
    //делал getInstance(session) то туда сессия пустая попадала тк, она после инициализации как раз таки создавалась
    // а потом была проблема что сессия закрывалась, вообщем как сделал щас то работает, потом если что перепишу как
    // sping гляну. больше на данный момент ничего не придумал.
    @BeforeEach
    public void initAddressRepository() {
        repository = new AddressRepository(session);
    }

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
        session.clear(); // вот тут же надо clear? так как на уровне сессии у нас адресс ещё остаётся(в других как будто бы тоже надо)
        var user = address.getUser();

        Optional<AddressEntity> resultEntity = repository.findById(address.getId());
        Assertions.assertTrue(resultEntity.isPresent());
    }

    @Test
    void findAll() {
        AddressEntity address = getAddressEntity();
        repository.save(address);

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
