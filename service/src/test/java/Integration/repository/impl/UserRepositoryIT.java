package Integration.repository.impl;

import Integration.annotation.IT;
import Integration.util.ContainerStarter;
import com.nikita.shop.entity.Activity;
import com.nikita.shop.entity.Role;
import com.nikita.shop.entity.UserEntity;
import com.nikita.shop.entity.embeddable.UserActivity;
import com.nikita.shop.entity.embeddable.UserCredentials;
import com.nikita.shop.entity.embeddable.UserPersonalInfo;
import com.nikita.shop.database.repository.impl.UserRepository;
import com.nikita.shop.database.filter.UserFilter;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IT
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
public class UserRepositoryIT extends ContainerStarter {
    private final EntityManager entityManager;
    private final UserRepository userRepository;


    @Test
    void save() {
        UserEntity user = getUserEntity("31331", "FDSFDS");
        userRepository.save(user);

        entityManager.flush();
        entityManager.clear();

        Optional<UserEntity> result = userRepository.findById(user.getId());

        assertTrue(result.isPresent());
    }

    @Test
    void findAll() {
        UserEntity user1 = getUserEntity("8939193", "Ffasf");
        UserEntity user2 = getUserEntity("8939193", "Ffasf");
        userRepository.save(user1);
        userRepository.save(user2);

        entityManager.flush();
        entityManager.clear();

        List<UserEntity> result = userRepository.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void findById() {
        UserEntity user1 = getUserEntity("8939193", "Ffasf");
        userRepository.save(user1);
        entityManager.flush();
        entityManager.clear();

        Optional<UserEntity> result = userRepository.findById(user1.getId());

        assertTrue(result.isPresent());
        assertEquals(user1.getId(), result.get().getId());
    }

    @Test
    void update() {
        UserEntity user1 = getUserEntity("8939193", "Ffasf");
        userRepository.save(user1);
        entityManager.flush();
        entityManager.clear();
        Optional<UserEntity> user = userRepository.findById(user1.getId());
        UserEntity updateUser = user.get();
        updateUser.setPhoneNumber("899999");
        userRepository.save(updateUser);

        entityManager.flush();
        entityManager.clear();

        Optional<UserEntity> result = userRepository.findById(updateUser.getId());
        assertTrue(result.isPresent());
        assertEquals("899999", result.get().getPhoneNumber());
    }

    @Test
    void delete() {
        UserEntity user1 = getUserEntity("8939193", "Ffasf");
        userRepository.save(user1);
        userRepository.deleteById(user1.getId());

        entityManager.flush();
        entityManager.clear();

        Optional<UserEntity> result = userRepository.findById(user1.getId());

        assertFalse(result.isPresent());
    }

    @Test
    void findAllByFilter_WithFilter() {
        UserEntity user1 = getUserEntity("8939193", "Ffasf", Role.USER);
        UserEntity user2 = getUserEntity("893944193", "Ff44asf", Role.ADMIN);
        userRepository.save(user1);
        userRepository.save(user2);

        UserFilter filter = UserFilter.builder()
                .role(Role.USER)
                .build();
        Pageable pageable = PageRequest.of(0, 10);

        entityManager.flush();
        entityManager.clear();

        Page<UserEntity> resultPage = userRepository.findAllByFilter(filter, pageable);

        assertEquals(1, resultPage.getTotalElements());
        assertEquals(Role.USER, resultPage.getContent().get(0).getRole());
    }

    @Test
    void findAllByFilter_NoFilter() {
        UserEntity user1 = getUserEntity("8939193", "Ffasf", Role.USER);
        UserEntity user2 = getUserEntity("893944193", "Ff44asf", Role.ADMIN);
        userRepository.save(user1);
        userRepository.save(user2);

        UserFilter filter = UserFilter.builder()
                .build();
        Pageable pageable = PageRequest.of(0, 10);

        entityManager.flush();
        entityManager.clear();

        Page<UserEntity> resultPage = userRepository.findAllByFilter(filter, pageable);

        assertEquals(2, resultPage.getTotalElements());
    }

    @Test
    void findAllByPhoneNumberAndEmail() {
        userRepository.save(getUserEntity("1234567890", "test1@mail.com"));
        userRepository.save(getUserEntity("1234567890", "test2@mail.com"));
        entityManager.flush();
        entityManager.clear();

        Page<UserEntity> result = userRepository.findAllByEmailAndPhoneNumber(
                "test1@mail.com",
                "1234567890",
                PageRequest.of(0, 10)
        );

        assertThat(result.getContent()).hasSize(1);
        assertEquals(result.getContent().get(0).getCredentials().getEmail(),"test1@mail.com");
    }

    @Test
    void findAllByActivity() {
        userRepository.save(getUserEntity(Activity.BLOCKED));
        userRepository.save(getUserEntity(Activity.ALLOWED));
        entityManager.flush();
        entityManager.clear();

        Page<UserEntity> result = userRepository.findAllByActivity(
                Activity.ALLOWED,
                PageRequest.of(0, 10)
        );

        assertEquals(1, result.getTotalElements());
        assertEquals(Activity.ALLOWED, result.getContent().get(0).getUserActivity().getActivity());
    }

    private UserEntity getUserEntity(String phoneNumber, String email) {
        return UserEntity.builder()
                .role(Role.ADMIN)
                .phoneNumber(phoneNumber)
                .personalInfo(UserPersonalInfo.builder()
                        .image("f")
                        .birthDate(LocalDate.of(2000, 10, 12))
                        .lastname("F")
                        .firstname("M")
                        .build())
                .credentials(UserCredentials.builder()
                        .password("12456")
                        .emailVerified(true)
                        .email(email)
                        .build())
                .userActivity(UserActivity.builder()
                        .registrationDate(Instant.now())
                        .activity(Activity.ALLOWED)
                        .lastLogin(Instant.now())
                        .build())
                .build();
    }

    private UserEntity getUserEntity(String phoneNumber, String email, Role role) {
        return UserEntity.builder()
                .role(role)
                .phoneNumber(phoneNumber)
                .personalInfo(UserPersonalInfo.builder()
                        .image("f")
                        .birthDate(LocalDate.of(2000, 10, 12))
                        .lastname("F")
                        .firstname("M")
                        .build())
                .credentials(UserCredentials.builder()
                        .password("12456")
                        .emailVerified(true)
                        .email(email)
                        .build())
                .userActivity(UserActivity.builder()
                        .registrationDate(Instant.now())
                        .activity(Activity.ALLOWED)
                        .lastLogin(Instant.now())
                        .build())
                .build();
    }
    private UserEntity getUserEntity(Activity activity) {
        return UserEntity.builder()
                .role(Role.ADMIN)
                .phoneNumber("fdfdfd")
                .personalInfo(UserPersonalInfo.builder()
                        .image("f")
                        .birthDate(LocalDate.of(2000, 10, 12))
                        .lastname("F")
                        .firstname("M")
                        .build())
                .credentials(UserCredentials.builder()
                        .password("12456")
                        .emailVerified(true)
                        .email("fdfdf")
                        .build())
                .userActivity(UserActivity.builder()
                        .registrationDate(Instant.now())
                        .activity(activity)
                        .lastLogin(Instant.now())
                        .build())
                .build();
    }

}
