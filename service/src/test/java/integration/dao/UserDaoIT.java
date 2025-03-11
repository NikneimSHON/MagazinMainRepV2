package integration.dao;

import com.nikita.shop.dao.UserDao;
import com.nikita.shop.dto.UserFilter;
import com.nikita.shop.entity.ActivityType;
import com.nikita.shop.entity.Role;
import com.nikita.shop.entity.UserEntity;
import com.nikita.shop.entity.embeddable.UserActivity;
import com.nikita.shop.entity.embeddable.UserCredentials;
import com.nikita.shop.entity.embeddable.UserPersonalInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.TransactionManager;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public class UserDaoIT extends TransactionManager {
    private final UserDao dao = UserDao.getInstance();

    @Test
    void findUsersByFirstNameAndLastName() {

        UserFilter filter = UserFilter.builder().
                firstName("first")
                .lastName("last")
                .build();
        session.persist(getUserEntity());
        session.clear();

        List<UserEntity> resultList = dao.findUsersByFirstNameAndLastName(session, filter);
        for (UserEntity user : resultList) {
            Assertions.assertEquals(user.getPersonalInfo().getFirstname(), filter.getFirstName());
            Assertions.assertEquals(user.getPersonalInfo().getLastname(), filter.getLastName());
        }

    }

    private UserEntity getUserEntity() {
        return UserEntity.builder()
                .role(Role.ADMIN)
                .phoneNumber("895200000")
                .personalInfo(UserPersonalInfo.builder()
                        .image("f")
                        .birthDate(LocalDate.of(2000, 10, 12))
                        .lastname("firstName")
                        .firstname("lastName")
                        .build())
                .credentials(UserCredentials.builder()
                        .password("12456")
                        .emailVerified(true)
                        .email("fafsa@mail.ru")
                        .build())
                .activity(UserActivity.builder()
                        .registrationDate(Instant.now())
                        .activity(ActivityType.ALLOWED)
                        .lastLogin(Instant.now())
                        .build())
                .build();
    }
}
