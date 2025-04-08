package com.nikita.shop.database.repository.impl;

import com.nikita.shop.dto.UserFilter;
import com.nikita.shop.database.entity.UserEntity;
import com.nikita.shop.database.repository.QPredicate;
import com.nikita.shop.database.repository.RepositoryBase;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import static com.nikita.shop.database.entity.QUserEntity.userEntity;

import java.util.List;

@Repository
public class UserRepository extends RepositoryBase<Long, UserEntity> {

    public UserRepository(EntityManager entityManager) {
        super(entityManager, UserEntity.class);

    }

    public List<UserEntity> findUserWithFilter(UserFilter filter, int limit, int offset) {
        var predicate = QPredicate.builder()
                .add(filter.getFirstName(), userEntity.personalInfo.firstname::eq)
                .add(filter.getLastName(), userEntity.personalInfo.lastname::eq)
                .add(filter.getPhoneNumber(), userEntity.phoneNumber::eq)
                .add(filter.getBeforeBirthday(), userEntity.personalInfo.birthDate::loe)
                .add(filter.getAfterBirthday(), userEntity.personalInfo.birthDate::goe)
                .add(filter.getActivity(), userEntity.activity.activity::eq)
                .add(filter.getRole(), userEntity.role::eq)
                .buildAnd();

        return new JPAQuery<UserEntity>(super.getEntityManager())
                .select(userEntity)
                .from(userEntity)
                .where(predicate)
                .limit(limit)
                .offset(offset)
                .fetch();

    }


}
