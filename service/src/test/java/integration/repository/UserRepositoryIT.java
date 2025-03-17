package integration.repository;

import com.nikita.shop.dto.UserFilter;
import com.nikita.shop.entity.Activity;
import com.nikita.shop.entity.Role;
import com.nikita.shop.entity.UserEntity;
import com.nikita.shop.entity.embeddable.UserActivity;
import com.nikita.shop.entity.embeddable.UserCredentials;
import com.nikita.shop.entity.embeddable.UserPersonalInfo;
import com.nikita.shop.repository.impl.AddressRepository;
import com.nikita.shop.repository.impl.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.TransactionManager;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public class UserRepositoryIT extends TransactionManager {
    private UserRepository userRepository;

    @BeforeEach
    public void initAddressRepository() {
        userRepository = new UserRepository(session);
    }
    /*
     public List<UserEntity> findUsersWithFilterFirstAndLastNameAndEmailAndPhoneNumAndBirthDayAndActivityAndRole(EntityManager entityManager, UserFilter filter, int limit, int offset) {
        var predicate = QPredicate.builder()
                .add(filter.getFirstName(), userEntity.personalInfo.firstname::eq)
                .add(filter.getLastName(), userEntity.personalInfo.lastname::eq)
                .add(filter.isEmail_verified(), userEntity.credentials.emailVerified::eq)
                .add(filter.getPhoneNumber(), userEntity.phoneNumber::eq)
                .add(filter.getBeforeBirthday(), userEntity.personalInfo.birthDate::loe)
                .add(filter.getAfterBirthday(), userEntity.personalInfo.birthDate::goe)
                .add(filter.getActivity(), userEntity.activity.activity::eq)
                .add(filter.getRole(), userEntity.role::eq)
                .buildOr();
     */

    @Test
    void findUsersWithFilterFirstNameAndLastNameAndEmailAndActivity() {

        UserFilter filter = UserFilter.builder().
                firstName("first")
                .lastName("last")
                .email("email")
                .activity(Activity.ALLOWED)
                .build();
        session.persist(getUserEntity());
        session.clear();

        List<UserEntity> resultList = userRepository.findUsersWithFilterFirstAndLastNameAndEmailAndPhoneNumAndBirthDayAndActivityAndRole(session, filter, 10, 0);
        for (UserEntity user : resultList) {
            Assertions.assertEquals(user.getPersonalInfo().getFirstname(), filter.getFirstName());
            Assertions.assertEquals(user.getPersonalInfo().getLastname(), filter.getLastName());
            Assertions.assertEquals(user.getCredentials().getEmail(), filter.getEmail());
            Assertions.assertEquals(user.getActivity().getActivity(), Activity.ALLOWED);
        }

    }

    private UserEntity getUserEntity() {
        return UserEntity.builder()
                .role(Role.ADMIN)
                .phoneNumber("895200000")
                .personalInfo(UserPersonalInfo.builder()
                        .image("f")
                        .birthDate(LocalDate.of(2000, 10, 12))
                        .lastname("last")
                        .firstname("first")
                        .build())
                .credentials(UserCredentials.builder()
                        .password("12456")
                        .emailVerified(true)
                        .email("email")
                        .build())
                .activity(UserActivity.builder()
                        .registrationDate(Instant.now())
                        .activity(Activity.ALLOWED)
                        .lastLogin(Instant.now())
                        .build())
                .build();
    }


}
