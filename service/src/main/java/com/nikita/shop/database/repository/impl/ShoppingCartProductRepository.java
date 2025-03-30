package com.nikita.shop.database.repository.impl;

import com.nikita.shop.database.entity.ShoppingCartProductEntity;
import com.nikita.shop.database.repository.RepositoryBase;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class ShoppingCartProductRepository extends RepositoryBase<Long, ShoppingCartProductEntity> {

    public ShoppingCartProductRepository(EntityManager entityManager) {
        super(entityManager, ShoppingCartProductEntity.class);
    }

}
