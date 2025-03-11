package com.nikita.shop.dao;

import com.nikita.shop.dto.ProductFilter;
import com.nikita.shop.dto.UserFilter;
import com.nikita.shop.entity.ProductEntity;
import com.nikita.shop.entity.UserEntity;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.UtilityClass;
import org.hibernate.Session;
import org.hibernate.graph.GraphSemantic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.nikita.shop.entity.QProductEntity.productEntity;
import static com.nikita.shop.entity.QUserEntity.userEntity;

public final class UserDao {

    private static UserDao instance;

    private UserDao() {

    }

    public static UserDao getInstance() {
        if (instance == null) {
            instance = new UserDao();
            return instance;
        }
        return instance;
    }

    public List<UserEntity> findUsersByFirstNameAndLastName(Session session, UserFilter filter) {
        var predicate = QPredicate.builder()
                .add(filter.getFirstName(), userEntity.personalInfo.firstname::eq)
                .add(filter.getLastName(), userEntity.personalInfo.lastname::eq)
                .buildAnd();
        return new JPAQuery<UserEntity>(session)
                .select(userEntity)
                .from(userEntity)
                .where(predicate)
                .setHint(GraphSemantic.LOAD.getJakartaHintName(), session.getEntityGraph("WithFirstNameAndLastName"))
                .fetch();
    }

}
