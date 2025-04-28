package com.nikita.shop.database.repository.filter.Impl;

import com.nikita.shop.entity.UserEntity;
import com.nikita.shop.database.repository.filter.FilterUserRepository;
import com.nikita.shop.database.repository.filter.QPredicate;
import com.nikita.shop.database.filter.UserFilter;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.nikita.shop.entity.QUserEntity.userEntity;

public class FilterUserRepositoryImpl implements FilterUserRepository {
    private final JPAQueryFactory queryFactory;

    public FilterUserRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<UserEntity> findAllByFilter(UserFilter filter, Pageable pageable) {
        var predicate = predicateBuilder(filter);

        List<UserEntity> resultList = queryFactory
                .select(userEntity)
                .from(userEntity)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long countElement = queryFactory
                .select(userEntity)
                .from(userEntity)
                .where(predicate)
                .fetch().size();

        return new PageImpl<>(resultList, pageable, countElement);
    }

    private Predicate predicateBuilder(UserFilter filter) {
        return QPredicate.builder()
                .add(filter.getFirstName(), userEntity.personalInfo.firstname::eq)
                .add(filter.getLastName(), userEntity.personalInfo.lastname::eq)
                .add(filter.getPhoneNumber(), userEntity.phoneNumber::eq)
                .add(filter.getBeforeBirthday(), userEntity.personalInfo.birthDate::loe)
                .add(filter.getAfterBirthday(), userEntity.personalInfo.birthDate::goe)
                .add(filter.getActivity(), userEntity.userActivity.activity::eq)
                .add(filter.getRole(), userEntity.role::eq)
                .buildAnd();

    }
}
