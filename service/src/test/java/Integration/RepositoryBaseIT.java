package Integration;

import Integration.annotation.IT;
import com.nikita.shop.database.repository.impl.AddressRepository;
import com.nikita.shop.database.entity.Activity;
import com.nikita.shop.database.entity.AddressEntity;
import com.nikita.shop.database.entity.Role;
import com.nikita.shop.database.entity.UserEntity;
import com.nikita.shop.database.entity.embeddable.AddressInfo;
import com.nikita.shop.database.entity.embeddable.UserActivity;
import com.nikita.shop.database.entity.embeddable.UserCredentials;
import com.nikita.shop.database.entity.embeddable.UserPersonalInfo;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import Integration.util.ContainerStarter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@IT
public class RepositoryBaseIT extends ContainerStarter {

    @Autowired
    private AddressRepository repository;
    @Autowired
    private EntityManager entityManager;

    @Test
    void update() {
        // Given
        AddressEntity address = getAddressEntity();
        repository.save(address);
        String updatedCity = "Fake";

        // When
        address.getAddressInfo().setCity(updatedCity);
        repository.update(address);

        // Then
        AddressEntity addressResult = repository.findById(address.getId()).orElseThrow();
        assertNotNull(addressResult);
        assertEquals(updatedCity, addressResult.getAddressInfo().getCity());
    }

    @Test
    void delete() {
        // Given
        AddressEntity address = getAddressEntity();
        repository.save(address);

        // When
        repository.delete(address);

        // Then
        Optional<AddressEntity> resultEntity = repository.findById(address.getId());
        assertFalse(resultEntity.isPresent());
    }

    @Test
    void findById() {
        // Given
        AddressEntity address = getAddressEntity();
        repository.save(address);
        entityManager.clear();
        Long addressId = address.getId();

        // When
        Optional<AddressEntity> resultEntity = repository.findById(addressId);

        // Then
        assertTrue(resultEntity.isPresent());
        assertEquals(addressId, resultEntity.get().getId());
    }

    @Test
    void findAll() {
        // Given
        AddressEntity address = getAddressEntity();
        repository.save(address);
        entityManager.clear();

        // When
        List<AddressEntity> resultEntities = repository.findAll();

        // Then
        assertFalse(resultEntities.isEmpty());
        assertTrue(resultEntities.stream()
                .anyMatch(a -> a.getId().equals(address.getId())));
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
