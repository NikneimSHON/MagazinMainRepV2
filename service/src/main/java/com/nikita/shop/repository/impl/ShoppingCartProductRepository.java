package com.nikita.shop.repository.impl;

import com.nikita.shop.entity.ShoppingCartProductEntity;
import com.nikita.shop.repository.RepositoryBase;
import jakarta.persistence.EntityManager;

public class ShoppingCartProductRepository extends RepositoryBase<Long, ShoppingCartProductEntity> {
    public ShoppingCartProductRepository(EntityManager entityManager) {
        super(entityManager, ShoppingCartProductEntity.class);
    }

}
