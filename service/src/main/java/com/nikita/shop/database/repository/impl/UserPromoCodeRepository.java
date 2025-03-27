package com.nikita.shop.database.repository.impl;

import com.nikita.shop.entity.UserPromoCodeEntity;
import com.nikita.shop.database.repository.RepositoryBase;
import jakarta.persistence.EntityManager;

public class UserPromoCodeRepository extends RepositoryBase<Long, UserPromoCodeEntity> {
    public UserPromoCodeRepository(EntityManager entityManager) {
        super(entityManager, UserPromoCodeEntity.class);
    }
}
