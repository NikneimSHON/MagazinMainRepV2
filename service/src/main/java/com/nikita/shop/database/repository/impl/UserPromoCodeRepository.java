package com.nikita.shop.database.repository.impl;

import com.nikita.shop.database.entity.UserPromoCodeEntity;
import com.nikita.shop.database.repository.RepositoryBase;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class UserPromoCodeRepository extends RepositoryBase<Long, UserPromoCodeEntity> {

    public UserPromoCodeRepository(EntityManager entityManager) {
        super(entityManager, UserPromoCodeEntity.class);
    }
}
