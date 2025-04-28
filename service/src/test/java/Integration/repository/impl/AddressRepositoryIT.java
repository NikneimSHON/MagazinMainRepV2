package Integration.repository.impl;

import Integration.annotation.IT;
import Integration.util.ContainerStarter;
import com.nikita.shop.entity.Activity;
import com.nikita.shop.entity.AddressEntity;
import com.nikita.shop.entity.Role;
import com.nikita.shop.entity.UserEntity;
import com.nikita.shop.entity.embeddable.AddressInfo;
import com.nikita.shop.entity.embeddable.UserActivity;
import com.nikita.shop.entity.embeddable.UserCredentials;
import com.nikita.shop.entity.embeddable.UserPersonalInfo;
import com.nikita.shop.database.repository.impl.AddressRepository;
import com.nikita.shop.database.repository.impl.UserRepository;
import com.nikita.shop.database.filter.AddressFilter;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IT
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
public class AddressRepositoryIT extends ContainerStarter {

    private final EntityManager entityManager;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @BeforeEach
    void setUp() {
        entityManager.clear();
    }

    @Test
    void save() {
        UserEntity user = userRepository.save(getUserEntity());
        AddressEntity address = addressRepository.save(getAddressEntity(user));

        entityManager.flush();
        entityManager.clear();

        AddressEntity foundAddress = addressRepository.findById(address.getId()).orElseThrow();

        assertEquals(address.getId(), foundAddress.getId());
    }

    @Test
    void findById() {
        UserEntity user = userRepository.save(getUserEntity());
        AddressEntity address = addressRepository.save(getAddressEntity(user));

        entityManager.flush();
        entityManager.clear();

        Optional<AddressEntity> foundAddress = addressRepository.findById(address.getId());

        assertEquals(address.getId(), foundAddress.get().getId());
        assertTrue(foundAddress.isPresent());
    }

    @Test
    void findAll() {
        UserEntity user1 = userRepository.save(getUserEntity());
        addressRepository.save(getAddressEntity(user1));

        UserEntity user2 = userRepository.save(getUserEntity2());
        addressRepository.save(getAddressEntity2(user2));

        entityManager.flush();
        entityManager.clear();

        List<AddressEntity> allAddresses = addressRepository.findAll();

        assertEquals(2, allAddresses.size());

    }

    @Test
    void update() {
        UserEntity user = userRepository.save(getUserEntity());
        AddressEntity address = addressRepository.save(getAddressEntity(user));
        String newCity = "Los Angeles";

        address.getAddressInfo().setCity(newCity);
        addressRepository.save(address);

        entityManager.flush();
        entityManager.clear();

        Optional<AddressEntity> updatedAddress = addressRepository.findById(address.getId());

        assertEquals(newCity, updatedAddress.get().getAddressInfo().getCity());
    }

    @Test
    void delete() {
        UserEntity user = userRepository.save(getUserEntity());
        AddressEntity address = addressRepository.save(getAddressEntity(user));

        addressRepository.delete(address);

        entityManager.flush();
        entityManager.clear();

        Optional<AddressEntity> deletedAddress = addressRepository.findById(address.getId());

        assertFalse(deletedAddress.isPresent());
    }

    @Test
    void findAllByFilter_NoFilter() {
        UserEntity user1 = userRepository.save(getUserEntity());
        addressRepository.save(getAddressEntity(user1));

        UserEntity user2 = userRepository.save(getUserEntity2());
        addressRepository.save(getAddressEntity2(user2));

        AddressFilter filter = AddressFilter.builder().build();
        Pageable pageable = PageRequest.of(0, 10);

        entityManager.flush();
        entityManager.clear();

        Page<AddressEntity> resultPage = addressRepository.findAllByFilter(filter, pageable);

        assertEquals(2, resultPage.getTotalElements());
    }

    @Test
    void deleteByCountryAndCity() {
        UserEntity user = userRepository.save(getUserEntity());
        AddressEntity address = addressRepository.save(getAddressEntity(user));

        int deletedCount = addressRepository.deleteByCountryAndCity(
                address.getAddressInfo().getCountry(),
                address.getAddressInfo().getCity()
        );

        assertEquals(1, deletedCount);
        assertFalse(addressRepository.findById(address.getId()).isPresent());
    }

    @Test
    void deleteByUserId() {
        UserEntity user = userRepository.save(getUserEntity());
        addressRepository.save(getAddressEntity(user));

        entityManager.flush();
        entityManager.clear();

        int deletedCount = addressRepository.deleteAllByUserId(user.getId());

        assertEquals(1, deletedCount);
        assertTrue(addressRepository.findAll().isEmpty());
    }

    @Test
    void findAllByFilter_WithFilter() {
        UserEntity user1 = userRepository.save(getUserEntity());
        addressRepository.save(getAddressEntity(user1));

        UserEntity user2 = userRepository.save(getUserEntity2());
        addressRepository.save(getAddressEntity2(user2));

        AddressFilter filter = AddressFilter.builder().city("New York").build();
        Pageable pageable = PageRequest.of(0, 10);

        entityManager.flush();
        entityManager.clear();

        Page<AddressEntity> resultPage = addressRepository.findAllByFilter(filter, pageable);

        assertEquals(1, resultPage.getTotalElements());
        assertEquals("New York", resultPage.getContent().get(0).getAddressInfo().getCity());
    }


    private AddressEntity getAddressEntity(UserEntity user) {
        return AddressEntity.builder()
                .addressInfo(AddressInfo.builder()
                        .city("New York")
                        .street("Nonexistent Street")
                        .houseNumber(999)
                        .apartmentNumber(99)
                        .country("Nonexistent Country")
                        .build())
                .user(user)
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
                .userActivity(UserActivity.builder()
                        .registrationDate(Instant.now())
                        .activity(Activity.ALLOWED)
                        .lastLogin(Instant.now())
                        .build())
                .build();
    }

    private AddressEntity getAddressEntity2(UserEntity user) {
        return AddressEntity.builder()
                .addressInfo(AddressInfo.builder()
                        .city("Insar")
                        .street("oktabrskay")
                        .houseNumber(73)
                        .apartmentNumber(1)
                        .country("Russia")
                        .build())
                .user(user)
                .build();
    }

    private UserEntity getUserEntity2() {
        return UserEntity.builder()
                .role(Role.ADMIN)
                .phoneNumber("895200011")
                .personalInfo(UserPersonalInfo.builder()
                        .image("fake")
                        .birthDate(LocalDate.of(2000, 10, 12))
                        .lastname("fake")
                        .firstname("fake")
                        .build())
                .credentials(UserCredentials.builder()
                        .password("fake")
                        .emailVerified(true)
                        .email("fake2@mail.ru")
                        .build())
                .userActivity(UserActivity.builder()
                        .registrationDate(Instant.now())
                        .activity(Activity.ALLOWED)
                        .lastLogin(Instant.now())
                        .build())
                .build();
    }
}
