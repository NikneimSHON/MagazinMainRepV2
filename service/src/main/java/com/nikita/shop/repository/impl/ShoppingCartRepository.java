package com.nikita.shop.repository.impl;

import com.nikita.shop.dto.ShoppingCartFilter;
import com.nikita.shop.entity.ProductEntity;
import com.nikita.shop.entity.ShoppingCartEntity;
import com.nikita.shop.repository.QPredicate;
import com.nikita.shop.repository.RepositoryBase;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import com.querydsl.core.Tuple;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.nikita.shop.entity.QProductEntity.productEntity;
import static com.nikita.shop.entity.QShoppingCartEntity.shoppingCartEntity;
import static com.nikita.shop.entity.QShoppingCartProductEntity.shoppingCartProductEntity;
import static com.nikita.shop.entity.QUserEntity.userEntity;

public class ShoppingCartRepository extends RepositoryBase<Long, ShoppingCartEntity> {
    public ShoppingCartRepository(EntityManager entityManager) {
        super(entityManager, ShoppingCartEntity.class);
    }

    public List<ShoppingCartEntity> findShoppingCartWithFilter(EntityManager entityManager, ShoppingCartFilter filter, int offset, int limit) {
        var predicate = QPredicate.builder()
                .add(filter.getStatus(), shoppingCartEntity.orderStatus::eq)
                .add(filter.getBeforeCreateTime(), shoppingCartEntity.shoppingCartDate.creatTime::loe)
                .add(filter.getAfterCreateTime(), shoppingCartEntity.shoppingCartDate.creatTime::goe)
                .add(filter.getUserId(), shoppingCartEntity.user.id::eq)
                .buildAnd();
        return new JPAQuery<ShoppingCartEntity>(entityManager)
                .select(shoppingCartEntity)
                .from(shoppingCartEntity)
                .where(predicate)
                .limit(limit)
                .offset(offset)
                .fetch();
    }


    public List<ProductEntity> findProductWithShoppingCart(EntityManager entityManager, ShoppingCartEntity shoppingCartEntity) {
        return new JPAQuery<ProductEntity>(entityManager)
                .select(productEntity)
                .from(shoppingCartProductEntity)
                .where(shoppingCartProductEntity.cart.id.eq(shoppingCartEntity.getId()))
                .fetch();
    }

    public Map<Long, Long> findUserAndShoppingCartByProductID(EntityManager entityManager, ProductEntity productEntity) {
        List<Tuple> results = new JPAQuery<Tuple>(entityManager)
                .select(userEntity.id, shoppingCartEntity.id)
                .from(shoppingCartProductEntity)
                .join(shoppingCartProductEntity.cart, shoppingCartEntity)
                .join(shoppingCartEntity.user, userEntity)
                .where(shoppingCartProductEntity.product.id.eq(productEntity.getId()))
                .fetch();

        if (!results.isEmpty()) {
            return results.stream()
                    .collect(Collectors.toMap(
                            tuple -> tuple.get(userEntity.id),
                            tuple -> tuple.get(shoppingCartEntity.id)
                    ));
        }
        return null;


    }


}
