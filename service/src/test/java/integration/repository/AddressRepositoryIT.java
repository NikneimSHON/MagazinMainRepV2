package integration.repository;

import com.nikita.shop.dto.AddressFilter;
import com.nikita.shop.entity.Activity;
import com.nikita.shop.entity.AddressEntity;
import com.nikita.shop.entity.Role;
import com.nikita.shop.entity.UserEntity;
import com.nikita.shop.entity.embeddable.AddressInfo;
import com.nikita.shop.entity.embeddable.UserActivity;
import com.nikita.shop.entity.embeddable.UserCredentials;
import com.nikita.shop.entity.embeddable.UserPersonalInfo;
import com.nikita.shop.repository.impl.AddressRepository;
import com.nikita.shop.repository.impl.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.Cleanup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.TransactionManager;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public class AddressRepositoryIT extends TransactionManager {
    private AddressRepository addressRepository;
    private UserRepository userRepository;

    @BeforeEach
    public void initAddressRepository() {
        addressRepository = new AddressRepository(session);
        userRepository = new UserRepository(session);
    }

    @Test
    void findAddressWithFilterUserAndStreetAndHouseNumber() {
        var user = getUserEntity();
        userRepository.save(user);

        AddressEntity addressEntity2 = getAddressEntityTryUser(user);
        addressRepository.save(addressEntity2);

        var addressFilter = AddressFilter.builder()
                .user(user)
                .street("Moskovskay")
                .houseNumber(41)
                .build();

        addressRepository.save(getAddressEntity());

        session.clear();

        List<AddressEntity> resultList = addressRepository.findAddressWithFilterUserAndCityAndStreetAndApartAndHouseNumAndCounty(session, addressFilter, 10, 0);
        if (resultList != null && !resultList.isEmpty()) {
            Assertions.assertEquals(resultList.size(), 1);

            AddressEntity address = resultList.getFirst();

            Assertions.assertEquals(address.getAddressInfo().getHouseNumber(), 41);
            Assertions.assertEquals(address.getAddressInfo().getStreet(), "Moskovskay");
            Assertions.assertEquals(address.getUser(), user);
        } else {
            Assertions.fail("Адрес с указанными фильтрами не найден!");
        }
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

    private AddressEntity getAddressEntityTryUser(UserEntity user) {
        return AddressEntity.builder()
                .addressInfo(AddressInfo.builder()
                        .country("Russia")
                        .city("London")
                        .street("Moskovskay")
                        .houseNumber(41)
                        .apartmentNumber(23)
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
                .activity(UserActivity.builder()
                        .registrationDate(Instant.now())
                        .activity(Activity.ALLOWED)
                        .lastLogin(Instant.now())
                        .build())
                .build();
    }

}
