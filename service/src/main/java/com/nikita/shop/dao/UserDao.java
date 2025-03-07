package com.nikita.shop.dao;

import com.nikita.shop.entity.UserEntity;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.UtilityClass;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

import static com.nikita.shop.entity.QUserEntity.userEntity;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
@UtilityClass
public final class UserDao {

    private final static UserDao INSTANCE = new UserDao();

    public static UserDao getInstance() {
        return INSTANCE;
    }


}
