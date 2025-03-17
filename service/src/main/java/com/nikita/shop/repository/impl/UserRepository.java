package com.nikita.shop.repository.impl;

import com.nikita.shop.dto.UserFilter;
import com.nikita.shop.entity.UserEntity;
import com.nikita.shop.repository.QPredicate;
import com.nikita.shop.repository.RepositoryBase;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.graph.GraphSemantic;

import java.util.List;

import static com.nikita.shop.entity.QUserEntity.userEntity;

public class UserRepository extends RepositoryBase<Long, UserEntity> {

    public UserRepository(EntityManager entityManager) {
        super(entityManager, UserEntity.class);
    }

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

        return new JPAQuery<UserEntity>(entityManager)
                .select(userEntity)
                .from(userEntity)
                .where(predicate)
                .limit(limit)
                .offset(offset)
                .fetch();

    }


}
