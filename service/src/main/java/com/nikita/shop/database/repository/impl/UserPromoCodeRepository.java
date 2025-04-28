package com.nikita.shop.database.repository.impl;

import com.nikita.shop.entity.UserPromoCodeEntity;
import com.nikita.shop.database.repository.filter.FilterUserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPromoCodeRepository extends JpaRepository<UserPromoCodeEntity, Long>, FilterUserRepository {


}
