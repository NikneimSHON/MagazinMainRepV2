package com.nikita.shop.database.repository.filter;

import com.nikita.shop.entity.UserEntity;
import com.nikita.shop.database.filter.UserFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FilterUserRepository {
    Page<UserEntity> findAllByFilter(UserFilter filter, Pageable pageable);

}
